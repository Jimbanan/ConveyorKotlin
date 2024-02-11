package org.example.conveyorkotlin.dto

import java.math.BigDecimal

data class CreditDTO(
    var amount: BigDecimal,
    var term: Int,
    var monthlyPayment: BigDecimal,
    var rate: BigDecimal,
    var psk: BigDecimal,
    var isInsuranceEnabled: Boolean,
    var isSalaryClient: Boolean,
    var paymentSchedule: List<PaymentScheduleElement>
)


