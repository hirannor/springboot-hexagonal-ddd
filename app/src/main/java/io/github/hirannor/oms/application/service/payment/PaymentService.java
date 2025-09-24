package io.github.hirannor.oms.application.service.payment;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.application.port.payment.PaymentGateway;
import io.github.hirannor.oms.application.port.payment.PaymentItem;
import io.github.hirannor.oms.application.port.payment.PaymentRequest;
import io.github.hirannor.oms.application.usecase.payment.HandlePaymentCallback;
import io.github.hirannor.oms.application.usecase.payment.PaymentCallbackHandling;
import io.github.hirannor.oms.application.usecase.payment.PaymentInitialization;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.order.OrderRepository;
import io.github.hirannor.oms.domain.order.command.InitializePayment;
import io.github.hirannor.oms.domain.order.command.PaymentInstruction;
import io.github.hirannor.oms.domain.payment.Payment;
import io.github.hirannor.oms.domain.payment.PaymentMethod;
import io.github.hirannor.oms.domain.payment.PaymentRepository;
import io.github.hirannor.oms.domain.payment.command.StartPayment;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.domain.product.ProductRepository;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationService
class PaymentService implements PaymentInitialization, PaymentCallbackHandling {
    private static final Logger LOGGER = LogManager.getLogger(
            PaymentService.class
    );
    private final PaymentRepository payments;
    private final OrderRepository orders;
    private final PaymentGateway payment;
    private final ProductRepository products;
    private final Outbox outboxes;

    private final BiFunction<OrderItem, Map<ProductId, Product>, PaymentItem> mapOrderItemToPaymentItem;

    @Autowired
    PaymentService(final PaymentRepository payments,
                   final OrderRepository orders,
                   final PaymentGateway payment,
                   final ProductRepository products,
                   final Outbox outboxes) {
        this.payments = payments;
        this.orders = orders;
        this.payment = payment;
        this.products = products;
        this.outboxes = outboxes;
        this.mapOrderItemToPaymentItem = new OrderItemToPaymentItemMapper();
    }

    @Override
    public PaymentInstruction initialize(final InitializePayment command) {
        if (command == null) throw new IllegalArgumentException("InitializePayment is null");

        LOGGER.info("Start payment initialization for orderId={}", command.orderId().asText());

        final Order order = orders.findBy(command.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(command.orderId()));

        final List<ProductId> productIds = order.orderItems()
                .stream()
                .map(OrderItem::productId)
                .toList();

        final Map<ProductId, Product> indexedProducts = products.findAllBy(productIds)
                .stream()
                .collect(Collectors.toMap(Product::id, p -> p));

        final List<PaymentItem> paymentItems = order.orderItems()
                .stream()
                .map(mapOrderItemToPaymentItem(indexedProducts))
                .toList();

        final PaymentInstruction instruction = payment.initialize(
                PaymentRequest.create(
                        order.id(),
                        paymentItems,
                        order.totalPrice(),
                        PaymentMethod.CARD
                )
        );

        final StartPayment startPayment = StartPayment.issue(
                order.id(),
                order.totalPrice(),
                instruction.providerReference()
        );

        final Payment payment = Payment.start(startPayment);
        payments.save(payment);

        payment.events().forEach(outboxes::save);
        payment.clearEvents();

        LOGGER.info("Finished payment initialization for orderId={}, paymentId={}, newStatus={}",
                order.id().asText(),
                payment.id().asText(),
                order.status()
        );

        return instruction;
    }

    @Override
    public void handle(final HandlePaymentCallback command) {
        if (command == null) throw new IllegalArgumentException("HandlePaymentCallback is null");
        LOGGER.info("Start processing Stripe payment callback...");

        payment.processCallback(command.payload(), command.signatureHeader())
                .ifPresentOrElse(paymentReceipt -> {
                            LOGGER.info("Received payment callback: status={} orderId={}",
                                    paymentReceipt.status(),
                                    paymentReceipt.orderId().asText()
                            );

                            final Payment toPersist = payments.findBy(paymentReceipt.orderId())
                                    .orElseThrow(failBecausePaymentWasNotFoundBy(paymentReceipt.orderId()));

                            toPersist.applyReceipt(paymentReceipt);
                            payments.save(toPersist);

                            toPersist.events().forEach(outboxes::save);
                            toPersist.clearEvents();


                            LOGGER.info("Payment callback successfully processed for orderId={}",
                                    paymentReceipt.orderId().asText()
                            );
                        },
                        () -> LOGGER.warn("Skipping payment callback handling")
                );
    }

    private Function<OrderItem, PaymentItem> mapOrderItemToPaymentItem(final Map<ProductId, Product> productMap) {
        return item -> mapOrderItemToPaymentItem.apply(item, productMap);
    }

    private Supplier<IllegalArgumentException> failBecauseOrderWasNotFoundBy(final OrderId order) {
        return () -> new IllegalArgumentException("Order was not found with by " + order);
    }

    private Supplier<IllegalArgumentException> failBecausePaymentWasNotFoundBy(final OrderId order) {
        return () -> new IllegalArgumentException("Payment was not found with by " + order);
    }
}
