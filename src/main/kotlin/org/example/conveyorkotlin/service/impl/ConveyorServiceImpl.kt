package org.example.conveyorkotlin.service.impl

import org.example.conveyorkotlin.dto.*
import org.example.conveyorkotlin.enums.EmploymentStatus
import org.example.conveyorkotlin.enums.Genders
import org.example.conveyorkotlin.enums.MaritalStatus
import org.example.conveyorkotlin.enums.Position
import org.example.conveyorkotlin.exceptions.ScoringException
import org.example.conveyorkotlin.service.ConveyorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class ConveyorServiceImpl : ConveyorService {

    val baseRate: BigDecimal = BigDecimal.valueOf(20)

    @Autowired
    lateinit var calculation: CalculationService

    var numberOffer: Int = 1

    override fun getOffers(loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO> {

        val loanOfferList: MutableList<LoanOfferDTO> = ArrayList()
        loanOfferList.add(
            formationOfOffers(
                loanApplicationRequestDTO,
                isInsuranceEnabled = true,
                isSalaryClient = true
            )
        )
        loanOfferList.add(
            formationOfOffers(
                loanApplicationRequestDTO,
                isInsuranceEnabled = true,
                isSalaryClient = false
            )
        )
        loanOfferList.add(
            formationOfOffers(
                loanApplicationRequestDTO,
                isInsuranceEnabled = false,
                isSalaryClient = false
            )
        )
        loanOfferList.add(
            formationOfOffers(
                loanApplicationRequestDTO,
                isInsuranceEnabled = false,
                isSalaryClient = true
            )
        )

        return loanOfferList
    }

    private fun formationOfOffers(
        loanApplicationRequestDTO: LoanApplicationRequestDTO,
        isInsuranceEnabled: Boolean,
        isSalaryClient: Boolean
    ): LoanOfferDTO {

        var rate: BigDecimal = baseRate
        var totalAmount: BigDecimal? = loanApplicationRequestDTO.amount

        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(1))
        }

        if (isInsuranceEnabled) {
            totalAmount =
                loanApplicationRequestDTO.amount.add(loanApplicationRequestDTO.amount.multiply(BigDecimal.valueOf(0.05)))
            rate = rate.subtract(BigDecimal.valueOf(3))
        }

        val monthlyPayment: BigDecimal =
            calculation.getMonthlyPayment(rate, totalAmount, loanApplicationRequestDTO.term)

        return LoanOfferDTO(
            numberOffer++.toLong(),
            loanApplicationRequestDTO.amount,
            calculation.getTotalAmount(monthlyPayment, loanApplicationRequestDTO.term),
            loanApplicationRequestDTO.term,
            monthlyPayment,
            rate,
            isInsuranceEnabled,
            isSalaryClient
        )

    }

    override fun loanCalculation(scoringDataDTO: ScoringDataDTO): CreditDTO {
        var rate: BigDecimal = baseRate
        var amount: BigDecimal = scoringDataDTO.amount
        val now = LocalDate.now()

        if (scoringDataDTO.isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(1))

        }

        if (scoringDataDTO.isInsuranceEnabled) {
            amount = amount.add(scoringDataDTO.amount?.multiply(BigDecimal.valueOf(0.05)))
            rate = rate.subtract(BigDecimal.valueOf(3))

        }

        if (scoringDataDTO.employment.employmentStatus === EmploymentStatus.UNEMPLOYED) {
            throw ScoringException("Work Status: Unemployed - Denied")
        } else if (scoringDataDTO.employment.employmentStatus === EmploymentStatus.SELF_EMPLOYED) {
            rate = rate.add(BigDecimal.valueOf(1))

        } else if (scoringDataDTO.employment.employmentStatus === EmploymentStatus.BUSINESS_OWNER) {
            rate = rate.add(BigDecimal.valueOf(3))

        }

        if (scoringDataDTO.employment.position === Position.MIDDLE_MANAGER) {
            rate = rate.subtract(BigDecimal.valueOf(2))

        } else if (scoringDataDTO.employment.position === Position.TOP_MANAGER) {
            rate = rate.add(BigDecimal.valueOf(4))

        }

        if (scoringDataDTO.employment.salary.multiply(BigDecimal.valueOf(20))
                .compareTo(scoringDataDTO.amount) < 0
        ) {
            throw ScoringException("Requested amount more than 20 salaries - Denied")
        }

        if (scoringDataDTO.maritalStatus === MaritalStatus.MARRIED) {
            rate = rate.subtract(BigDecimal.valueOf(3))

        } else if (scoringDataDTO.maritalStatus === MaritalStatus.DIVORCED) {
            rate = rate.add(BigDecimal.valueOf(3))
        }

        if (scoringDataDTO.dependentAmount > 1) {
            rate = rate.add(BigDecimal.valueOf(1))

        }

        if (ChronoUnit.YEARS.between(scoringDataDTO.birthdate, now) < 20) {
            throw ScoringException("Age under 20 - Denied")
        } else if (ChronoUnit.YEARS.between(scoringDataDTO.birthdate, now) > 60) {
            throw ScoringException("Age over 60 - Denied")
        }

        if (scoringDataDTO.gender === Genders.MALE && (ChronoUnit.YEARS.between(
                scoringDataDTO.birthdate,
                now
            ) >= 30)
            && (ChronoUnit.YEARS.between(scoringDataDTO.birthdate, now) <= 55)
        ) {
            rate = rate.subtract(BigDecimal.valueOf(3))

        } else if (scoringDataDTO.gender === Genders.WOMAN && (ChronoUnit.YEARS.between(
                scoringDataDTO.birthdate,
                now
            ) >= 35)
            && (ChronoUnit.YEARS.between(scoringDataDTO.birthdate, now) <= 60)
        ) {
            rate = rate.subtract(BigDecimal.valueOf(3))

        } else if (scoringDataDTO.gender === Genders.NOT_BINARY) {
            rate = rate.add(BigDecimal.valueOf(3))

        }

        if (scoringDataDTO.employment.workExperienceTotal < 12) {
            throw ScoringException("Total work experience less than 12 months - Denied")
        } else if (scoringDataDTO.employment.workExperienceTotal < 3) {
            throw ScoringException("Current work experience less than 3 months - Denied")
        }

        val monthlyPayment: BigDecimal = calculation.getMonthlyPayment(rate, amount, scoringDataDTO.term)


        return CreditDTO(
            amount = amount,
            term = scoringDataDTO.term,
            monthlyPayment = monthlyPayment,
            rate = rate,
            psk = calculation.getTotalAmount(monthlyPayment, scoringDataDTO.term),
            isInsuranceEnabled = scoringDataDTO.isInsuranceEnabled,
            isSalaryClient = scoringDataDTO.isSalaryClient,
            paymentSchedule = getPaymentScheduleElement(
                scoringDataDTO.term.toInt(),
                monthlyPayment,
                amount,
                calculation.getTotalAmount(monthlyPayment, scoringDataDTO.term)
            )

        )
    }

    fun getPaymentScheduleElement(
        term: Int,
        monthlyPayment: BigDecimal?,
        amount: BigDecimal?,
        totalAmount: BigDecimal
    ): List<PaymentScheduleElement> {
        val paymentScheduleElementList: MutableList<PaymentScheduleElement> = ArrayList()
        var now = LocalDate.now()
        var remainingDebt = totalAmount
        var totalPayment = BigDecimal.valueOf(0)

        for (i in 1..term) {
            val paymentScheduleElement = PaymentScheduleElement()
            paymentScheduleElement.number = i

            now = now.plusMonths(1)
            paymentScheduleElement.date = (now)

            totalPayment = totalPayment.add(monthlyPayment)
            paymentScheduleElement.totalPayment = totalPayment

            paymentScheduleElement.interestPayment = (calculation.getInterestPayment(monthlyPayment!!, amount!!, term))
            paymentScheduleElement.debtPayment = monthlyPayment
            remainingDebt = remainingDebt.subtract(monthlyPayment)
            paymentScheduleElement.remainingDebt = remainingDebt

            paymentScheduleElementList.add(paymentScheduleElement)
        }

        return paymentScheduleElementList
    }
}