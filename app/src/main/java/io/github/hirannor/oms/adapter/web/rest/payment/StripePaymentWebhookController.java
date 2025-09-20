package io.github.hirannor.oms.adapter.web.rest.payment;

import io.github.hirannor.oms.application.usecase.payment.HandlePaymentCallback;
import io.github.hirannor.oms.application.usecase.payment.PaymentCallbackHandling;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@DriverAdapter
@RestController
@RequestMapping("/api/payments")
class StripePaymentWebhookController {
    private final PaymentCallbackHandling payment;

    @Autowired
    StripePaymentWebhookController(final PaymentCallbackHandling payment) {
        this.payment = payment;
    }

    @PostMapping("/stripe/webhook")
    ResponseEntity<Void> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signatureHeader
    ) {
        payment.handle(
                HandlePaymentCallback.issue(
                        payload,
                        signatureHeader
                )
        );
        return ResponseEntity.ok().build();
    }
}
