package org.example.conveyorkotlin.service.impl

import lombok.extern.slf4j.Slf4j
import org.example.conveyorkotlin.service.ConveyorService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
@Slf4j
class CalculationService {
    fun getMonthlyInterest(rate: BigDecimal): BigDecimal {

        val monthlyInterest =
            rate.divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 5, RoundingMode.HALF_UP)


        return monthlyInterest
    }

    fun getMonthlyPayment(rate: BigDecimal, totalAmount: BigDecimal?, term: Int?): BigDecimal {

        val i = getMonthlyInterest(rate)

        val monthlyPayment = totalAmount?.multiply(
            i.add(
                i.divide(
                    term?.toLong()?.toInt()
                        ?.let { i.add(BigDecimal.valueOf(1)).pow(it).subtract(BigDecimal.valueOf(1)) },
                    5,
                    RoundingMode.HALF_UP
                )
            )
        )


        return monthlyPayment!!
    }

    fun getTotalAmount(monthlyPayment: BigDecimal, term: Int): BigDecimal {

        val totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term.toLong()))

        return totalAmount
    }

    fun getInterestPayment(monthlyPayment: BigDecimal, totalAmount: BigDecimal, term: Int): BigDecimal {
        val interestPayment =
            monthlyPayment.subtract(totalAmount.divide(BigDecimal.valueOf(term.toLong()), 5, RoundingMode.HALF_UP))


        return interestPayment
    }
}