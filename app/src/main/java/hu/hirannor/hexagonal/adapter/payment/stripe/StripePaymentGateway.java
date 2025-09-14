package hu.hirannor.hexagonal.adapter.payment.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import hu.hirannor.hexagonal.application.port.payment.*;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.payment.PaymentReceipt;
import hu.hirannor.hexagonal.domain.order.payment.PaymentStatus;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
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

    private final StripeClient client;
    private final StripeConfigurationProperties config;

    @Autowired
    StripePaymentGateway(final StripeClient client, final StripeConfigurationProperties config) {
       this(
           client,
           config,
           new PaymentMethodToTypeMapper(),
           new CurrencyToModelMapper(),
           new CurrencyModelToDomainMapper()
       );
    }

    StripePaymentGateway(final StripeClient client,
                         final StripeConfigurationProperties config,
                         final Function<PaymentMethod, SessionCreateParams.PaymentMethodType> mapPaymentMethodToType,
                         final Function<Currency, CurrencyModel> mapCurrencyToModel,
                         final Function<CurrencyModel, Currency> mapCurrencyModelToDomain) {
        this.client = client;
        this.config = config;
        this.mapPaymentMethodToType = mapPaymentMethodToType;
        this.mapCurrencyToModel = mapCurrencyToModel;
        this.mapCurrencyModelToDomain = mapCurrencyModelToDomain;
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
                    .putMetadata("orderId", payment.orderId().value());

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

            try {
                final CheckOutSessionModel checkoutSession = CheckOutSessionModel.from(event.getType());

                final Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() -> new IllegalStateException("Invalid session data"));

                final String orderId = session.getMetadata().get("orderId");

                final Currency currency = mapCurrencyModelToDomain.apply(
                        CurrencyModel.from(session.getCurrency().toUpperCase())
                );

                final Money amount = Money.of(
                        session.getAmountTotal() / 100.0,
                        currency
                );

                final PaymentStatus status = mapToPaymentStatus(checkoutSession);

                return PaymentReceipt.create(
                            OrderId.from(orderId),
                            status,
                            session.getId(),
                            amount
                );

            } catch (IllegalArgumentException ignored) {
                // NOOP, we don't care about other events
            }

            return PaymentReceipt.create(
                    OrderId.unknown(),
                    PaymentStatus.PENDING,
                    event.getId(),
                    Money.zero(Currency.EUR)
            );

        } catch (SignatureVerificationException e) {
            throw new IllegalArgumentException("Invalid Stripe webhook signature", e);
        }
    }

    private PaymentStatus mapToPaymentStatus(final CheckOutSessionModel checkoutSession) {
        return switch (checkoutSession) {
            case COMPLETED -> PaymentStatus.SUCCESS;
            case EXPIRED -> PaymentStatus.CANCELLED;
            case FAILED -> PaymentStatus.FAILURE;
        };
    }
}
