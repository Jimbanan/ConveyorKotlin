package org.example.conveyorkotlin.dto

import java.math.BigDecimal
import java.time.LocalDate

data class LoanApplicationRequestDTO(
    var amount: BigDecimal,
    var term: Int,
    var firstName: String,
    var lastName: String,
    var middleName: String,
    var email: String,
    var birthdate: LocalDate,
    var passportSeries: String,
    var passportNumber: String
)
