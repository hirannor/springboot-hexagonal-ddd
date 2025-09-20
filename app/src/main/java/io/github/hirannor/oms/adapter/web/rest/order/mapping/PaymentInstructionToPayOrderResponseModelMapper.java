package io.github.hirannor.oms.adapter.web.rest.order.mapping;

import io.github.hirannor.oms.adapter.web.rest.orders.model.MoneyModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.PayOrderResponseModel;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.command.PaymentInstruction;

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
