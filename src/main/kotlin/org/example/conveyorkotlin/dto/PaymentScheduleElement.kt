package org.example.conveyorkotlin.dto

import lombok.Setter
import java.math.BigDecimal
import java.time.LocalDate

@Setter
data class PaymentScheduleElement(
    var number: Int?,
    var date: LocalDate?,
    var totalPayment: BigDecimal?,
    var interestPayment: BigDecimal?,
    var debtPayment: BigDecimal?,
    var remainingDebt: BigDecimal?
) {
    constructor() : this(null, null, null, null, null, null)


}

