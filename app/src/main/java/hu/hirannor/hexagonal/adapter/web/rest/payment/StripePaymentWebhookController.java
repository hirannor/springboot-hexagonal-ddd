package hu.hirannor.hexagonal.adapter.web.rest.payment;

import hu.hirannor.hexagonal.application.usecase.payment.HandlePaymentCallback;
import hu.hirannor.hexagonal.application.usecase.payment.PaymentCallbackHandling;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@DriverAdapter
public class StripePaymentWebhookController {
    private final PaymentCallbackHandling payment;

    @Autowired
    public StripePaymentWebhookController(final PaymentCallbackHandling payment) {
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
