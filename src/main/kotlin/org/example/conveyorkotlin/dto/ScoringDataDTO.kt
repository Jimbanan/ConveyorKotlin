package org.example.conveyorkotlin.dto

import org.example.conveyorkotlin.enums.Genders
import org.example.conveyorkotlin.enums.MaritalStatus
import java.math.BigDecimal
import java.time.LocalDate

data class ScoringDataDTO(
    var amount: BigDecimal,
    var term: Int,
    var firstName: String,
    var lastName: String,
    var middleName: String,
    var gender: Genders,
    var birthdate: LocalDate,
    var passportSeries: String,
    var passportNumber: String,
    var passportIssueDate: LocalDate,
    var passportIssueBranch: String,
    var maritalStatus: MaritalStatus,
    var dependentAmount: Int,
    var employment: EmploymentDTO,
    var account: String,
    var isInsuranceEnabled: Boolean,
    var isSalaryClient: Boolean
)