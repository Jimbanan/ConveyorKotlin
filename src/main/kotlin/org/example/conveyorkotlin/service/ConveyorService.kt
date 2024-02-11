package org.example.conveyorkotlin.service

import org.example.conveyorkotlin.dto.CreditDTO
import org.example.conveyorkotlin.dto.LoanApplicationRequestDTO
import org.example.conveyorkotlin.dto.LoanOfferDTO
import org.example.conveyorkotlin.dto.ScoringDataDTO

interface ConveyorService {

    fun getOffers(loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO>

    fun loanCalculation(scoringDataDTO: ScoringDataDTO): CreditDTO

}