package hu.hirannor.hexagonal.adapter.payment.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import hu.hirannor.hexagonal.adapter.payment.stripe.mapping.CurrencyModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.payment.stripe.mapping.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.payment.stripe.mapping.PaymentMethodModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.payment.stripe.mapping.PaymentMethodToTypeMapper;
import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.port.payment.PaymentInitializationFailed;
import hu.hirannor.hexagonal.application.port.payment.PaymentItem;
import hu.hirannor.hexagonal.application.port.payment.PaymentRequest;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
import hu.hirannor.hexagonal.domain.order.payment.PaymentMethod;
import hu.hirannor.hexagonal.domain.order.payment.PaymentReceipt;
import hu.hirannor.hexagonal.domain.order.payment.PaymentStatus;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@DrivenAdapter
class StripePaymentGateway implements PaymentGateway {

    private final Function<PaymentMethod, SessionCreateParams.PaymentMethodType> mapPaymentMethodToType;
    private final Function<Currency, CurrencyModel> mapCurrencyToModel;
    private final Function<CurrencyModel, Currency> mapCurrencyModelToDomain;
    private final Function<PaymentMethodModel, PaymentMethod> mapPaymentMethodModelToDomain;

    private final StripeClient client;
    private final StripeConfigurationProperties config;

    @Autowired
    StripePaymentGateway(final StripeClient client, final StripeConfigurationProperties config) {
       this(
           client,
           config,
           new PaymentMethodToTypeMapper(),
           new CurrencyToModelMapper(),
           new CurrencyModelToDomainMapper(),
           new PaymentMethodModelToDomainMapper()
       );
    }

    StripePaymentGateway(final StripeClient client,
                         final StripeConfigurationProperties config,
                         final Function<PaymentMethod, SessionCreateParams.PaymentMethodType> mapPaymentMethodToType,
                         final Function<Currency, CurrencyModel> mapCurrencyToModel,
                         final Function<CurrencyModel, Currency> mapCurrencyModelToDomain,
                         final Function<PaymentMethodModel, PaymentMethod> mapPaymentMethodModelToDomain) {
        this.client = client;
        this.config = config;
        this.mapPaymentMethodToType = mapPaymentMethodToType;
        this.mapCurrencyToModel = mapCurrencyToModel;
        this.mapCurrencyModelToDomain = mapCurrencyModelToDomain;
        this.mapPaymentMethodModelToDomain = mapPaymentMethodModelToDomain;
    }

    @Override
    public PaymentInstruction initialize(final PaymentRequest payment) {
        try {
            final SessionCreateParams.PaymentMethodType methodType = mapPaymentMethodToType.apply(payment.method());

            final SessionCreateParams.Builder params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(config.getSuccessUrl() + payment.orderId().value())
                    .setCancelUrl(config.getFailureUrl() + payment.orderId().value())
                    .addPaymentMethodType(methodType)
                    .putMetadata("orderId", payment.orderId().value())
                    .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .putMetadata("orderId", payment.orderId().value())
                                    .build()
                    );

            for (final PaymentItem item : payment.items()) {
                final CurrencyModel currency = mapCurrencyToModel.apply(item.price().currency());
                params.addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity((long) item.quantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency.value().toLowerCase())
                                                .setUnitAmount(item.price().amount().movePointRight(2).longValueExact())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Product Id: " + item.productId().value())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );
            }


            final Session session = client.checkout().sessions().create(params.build());

            return PaymentInstruction.create(
                    payment.orderId(),
                    payment.totalAmount(),
                    session.getUrl()
                );
        } catch (final StripeException ignored) {
            throw new PaymentInitializationFailed("Stripe initialization failed");
        }
    }

    @Override
    public PaymentReceipt processCallback(final String payload, final String signatureHeader) {
        try {
            final Event event = Webhook.constructEvent(payload, signatureHeader, config.getWebHookSecret());
            final String type = event.getType();

            if (type.startsWith("checkout.session."))
                return handleCheckoutEvent(CheckOutSessionEvent.from(type), event);
            if (type.startsWith("payment_intent."))
                return handlePaymentIntentEvent(PaymentIntentEvent.from(type), event);
            if (type.startsWith("charge."))
                return handleChargeEvent(ChargeEvent.from(type), event);

            return PaymentReceipt.create(
                    "UNKNOWN",
                    "UNKNOWN",
                    PaymentMethod.CARD,
                    OrderId.unknown(),
                    PaymentStatus.PENDING,
                    event.getId(),
                    Money.zero(Currency.EUR)
            );

        } catch (SignatureVerificationException e) {
            throw new IllegalArgumentException("Invalid Stripe webhook signature", e);
        }
    }

    private PaymentReceipt handleCheckoutEvent(final CheckOutSessionEvent sessionEvent, final Event event) {
        final Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Invalid checkout session data"));

        final String orderId = session.getMetadata().get("orderId");
        final Currency currency = mapCurrencyModelToDomain.apply(CurrencyModel.from(session.getCurrency().toUpperCase()));
        final Money amount = Money.of(session.getAmountTotal() / 100.0, currency);

        final PaymentMethodModel methodModel = PaymentMethodModel.from(session.getPaymentMethodTypes().getFirst());
        final PaymentMethod method = mapPaymentMethodModelToDomain.apply(methodModel);

        return PaymentReceipt.create(
                session.getId(),
                session.getPaymentIntent(),
                method,
                OrderId.from(orderId),
                mapToPaymentStatus(sessionEvent),
                session.getId(),
                amount
        );
    }

    private PaymentReceipt handleChargeEvent(final ChargeEvent chargeEvent, final Event event) {
        final Charge charge = (Charge) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Invalid charge data"));

        final String orderId = charge.getMetadata().get("orderId");
        final Currency currency = mapCurrencyModelToDomain.apply(CurrencyModel.from(charge.getCurrency().toUpperCase()));
        final Money amount = Money.of(charge.getAmount() / 100.0, currency);

        return PaymentReceipt.create(
                charge.getId(),
                charge.getPaymentIntent(),
                PaymentMethod.CARD,
                OrderId.from(orderId),
                mapToPaymentStatus(chargeEvent),
                event.getId(),
                amount
        );
    }

    private PaymentReceipt handlePaymentIntentEvent(final PaymentIntentEvent intentEvent, final Event event) {
        final PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Invalid payment intent data"));

        final String orderId = intent.getMetadata().get("orderId");
        final Currency currency = mapCurrencyModelToDomain.apply(CurrencyModel.from(intent.getCurrency().toUpperCase()));
        final Money amount = Money.of(intent.getAmount() / 100.0, currency);

        final PaymentMethodModel methodModel = PaymentMethodModel.from(intent.getPaymentMethodTypes().getFirst());
        final PaymentMethod method = mapPaymentMethodModelToDomain.apply(methodModel);

        final String transactionId = intent.getLatestCharge() != null
                ? intent.getLatestCharge()
                : intent.getId();

        return PaymentReceipt.create(
                transactionId,
                intent.getId(),
                method,
                OrderId.from(orderId),
                mapToPaymentStatus(intentEvent),
                event.getId(),
                amount
        );
    }

    private PaymentStatus mapToPaymentStatus(final ChargeEvent event) {
        return switch (event) {
            case SUCCEEDED, DISPUTE_CLOSED -> PaymentStatus.SUCCESS;
            case FAILED -> PaymentStatus.FAILURE;
            case REFUNDED -> PaymentStatus.CANCELLED;
            case DISPUTE_CREATED -> PaymentStatus.PENDING;
        };
    }

    private PaymentStatus mapToPaymentStatus(final PaymentIntentEvent event) {
        return switch (event) {
            case PAYMENT_SUCCEEDED -> PaymentStatus.SUCCESS;
            case PAYMENT_FAILED -> PaymentStatus.FAILURE;
            case PAYMENT_CANCELED -> PaymentStatus.CANCELLED;
        };
    }

    private PaymentStatus mapToPaymentStatus(final CheckOutSessionEvent checkoutSession) {
        return switch (checkoutSession) {
            case COMPLETED, ASYNC_SUCCESS -> PaymentStatus.SUCCESS;
            case EXPIRED -> PaymentStatus.CANCELLED;
            case ASYNC_FAILED -> PaymentStatus.FAILURE;
        };
    }
}
