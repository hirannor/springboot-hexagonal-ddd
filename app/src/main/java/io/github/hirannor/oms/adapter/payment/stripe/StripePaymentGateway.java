package io.github.hirannor.oms.adapter.payment.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.hirannor.oms.adapter.payment.stripe.mapping.CurrencyModelToDomainMapper;
import io.github.hirannor.oms.adapter.payment.stripe.mapping.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.payment.stripe.mapping.PaymentMethodModelToDomainMapper;
import io.github.hirannor.oms.adapter.payment.stripe.mapping.PaymentMethodToTypeMapper;
import io.github.hirannor.oms.application.port.payment.PaymentGateway;
import io.github.hirannor.oms.application.port.payment.PaymentInitializationFailed;
import io.github.hirannor.oms.application.port.payment.PaymentItem;
import io.github.hirannor.oms.application.port.payment.PaymentRequest;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.command.PaymentInstruction;
import io.github.hirannor.oms.domain.payment.PaymentMethod;
import io.github.hirannor.oms.domain.payment.PaymentReceipt;
import io.github.hirannor.oms.domain.payment.PaymentStatus;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@DrivenAdapter
class StripePaymentGateway implements PaymentGateway {

    private static final Logger LOGGER = LogManager.getLogger(
            StripePaymentGateway.class
    );

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
                                                                .setName(item.productName())
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
                    session.getId(),
                    session.getUrl()
            );
        } catch (final StripeException ignored) {
            throw new PaymentInitializationFailed("Stripe initialization failed");
        }
    }

    @Override
    public Optional<PaymentReceipt> processCallback(final String payload, final String signatureHeader) {
        try {
            final Event event = Webhook.constructEvent(payload, signatureHeader, config.getWebHookSecret());
            final String type = event.getType();

            if (!type.startsWith("payment_intent.")) return Optional.empty();

            try {
                final PaymentReceipt receipt = handlePaymentIntentEvent(PaymentIntentEvent.from(type), event);
                return Optional.of(receipt);
            } catch (final IllegalArgumentException ex) {
                LOGGER.debug("Skipping process, because of: {}", ex.getMessage());
                return Optional.empty();
            }

        } catch (final SignatureVerificationException e) {
            throw new IllegalArgumentException("Invalid Stripe webhook signature", e);
        }
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


    private PaymentStatus mapToPaymentStatus(final PaymentIntentEvent event) {
        return switch (event) {
            case PAYMENT_SUCCEEDED -> PaymentStatus.SUCCEEDED;
            case PAYMENT_FAILED -> PaymentStatus.FAILED;
            case PAYMENT_CANCELED -> PaymentStatus.CANCELED;
        };
    }
}
