package hu.hirannor.hexagonal.adapter.web.rest.order.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.MoneyModel;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.PayOrderResponseModel;
import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;

import java.util.function.Function;

public class PaymentInstructionToPayOrderResponseModelMapper implements Function<PaymentInstruction, PayOrderResponseModel> {

    private final Function<Money, MoneyModel> mapMoneyToModel;

    public PaymentInstructionToPayOrderResponseModelMapper() {
        this.mapMoneyToModel = new MoneyToModelMapper();
    }

    @Override
    public PayOrderResponseModel apply(final PaymentInstruction instruction) {
        if (instruction == null) return null;

        return new PayOrderResponseModel()
                .orderId(instruction.orderId().asText())
                .paymentUrl(instruction.paymentUrl())
                .amount(mapMoneyToModel.apply(instruction.price()));
    }
}
