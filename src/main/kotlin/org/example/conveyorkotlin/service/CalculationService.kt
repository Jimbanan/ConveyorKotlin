package org.example.conveyorkotlin.service

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
interface CalculationService {

    fun getMonthlyInterest(rate: BigDecimal): BigDecimal

    fun getMonthlyPayment(rate: BigDecimal, totalAmount: BigDecimal?, term: Int?): BigDecimal

    fun getTotalAmount(monthlyPayment: BigDecimal, term: Int): BigDecimal

    fun getInterestPayment(monthlyPayment: BigDecimal, totalAmount: BigDecimal, term: Int): BigDecimal

}
