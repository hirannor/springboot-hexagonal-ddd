package hu.hirannor.hexagonal.application.service.payment;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.port.payment.PaymentItem;
import hu.hirannor.hexagonal.application.port.payment.PaymentRequest;
import hu.hirannor.hexagonal.application.usecase.payment.HandlePaymentCallback;
import hu.hirannor.hexagonal.application.usecase.payment.PaymentCallbackHandling;
import hu.hirannor.hexagonal.application.usecase.payment.PaymentInitialization;
import hu.hirannor.hexagonal.domain.order.*;
import hu.hirannor.hexagonal.domain.order.command.InitializePayment;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
import hu.hirannor.hexagonal.domain.payment.Payment;
import hu.hirannor.hexagonal.domain.payment.PaymentMethod;
import hu.hirannor.hexagonal.domain.payment.PaymentRepository;
import hu.hirannor.hexagonal.domain.payment.command.StartPayment;
import hu.hirannor.hexagonal.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@ApplicationService
class PaymentService implements PaymentInitialization, PaymentCallbackHandling {
    private static final Logger LOGGER = LogManager.getLogger(
        PaymentService.class
    );
    private final PaymentRepository payments;
    private final OrderRepository orders;
    private final PaymentGateway payment;

    private final Function<OrderItem, PaymentItem> mapOrderItemToPaymentItem;

    @Autowired
    PaymentService(final PaymentRepository payments,
                   final OrderRepository orders,
                   final PaymentGateway payment) {
        this.payments = payments;
        this.orders = orders;
        this.payment = payment;
        this.mapOrderItemToPaymentItem = new OrderItemToPaymentItemMapper();
    }

    @Override
    public PaymentInstruction initialize(final InitializePayment command) {
        if (command == null) throw new IllegalArgumentException("InitializePayment is null");

        LOGGER.info("Start payment initialization for orderId={}", command.orderId().asText());

        final Order order = orders.findBy(command.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(command.orderId()));

        final List<PaymentItem> paymentItems = order.orderItems()
                .stream()
                .map(mapOrderItemToPaymentItem)
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

        order.changeStatus(OrderStatus.PAYMENT_PENDING);
        orders.save(order);

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

                    final Order order = orders.findBy(paymentReceipt.orderId())
                        .orElseThrow(failBecauseOrderWasNotFoundBy(paymentReceipt.orderId()));

                    order.handlePaymentResult(paymentReceipt);
                    orders.save(order);

                    LOGGER.info("Payment callback successfully processed for orderId={}, newStatus={}",
                        paymentReceipt.orderId().asText(),
                        order.status()
                    );
                },
                    () -> LOGGER.warn("Skipping payment callback handling")
                );
    }

    private Supplier<IllegalArgumentException> failBecauseOrderWasNotFoundBy(final OrderId order) {
        return () -> new IllegalArgumentException("Order was not found with by " + order);
    }

    private Supplier<IllegalArgumentException> failBecausePaymentWasNotFoundBy(final OrderId order) {
        return () -> new IllegalArgumentException("Payment was not found with by " + order);
    }
}
