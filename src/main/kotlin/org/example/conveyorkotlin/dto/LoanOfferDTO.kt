package org.example.conveyorkotlin.dto

import java.math.BigDecimal

data class LoanOfferDTO(
    var applicationId: Long,
    var requestedAmount: BigDecimal,
    var totalAmount: BigDecimal,
    var term: Int,
    var monthlyPayment: BigDecimal,
    var rate: BigDecimal,
    var isInsuranceEnabled: Boolean,
    var isSalaryClient: Boolean
)