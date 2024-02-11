package org.example.conveyorkotlin.api

import org.example.conveyorkotlin.dto.CreditDTO
import org.example.conveyorkotlin.dto.LoanApplicationRequestDTO
import org.example.conveyorkotlin.dto.LoanOfferDTO
import org.example.conveyorkotlin.dto.ScoringDataDTO
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Validated
@RequestMapping("/conveyor")
interface ConveyorApi {


    @PostMapping("/offers")
    fun offers(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO>?

    @PostMapping("/calculation")
    fun calculation(@RequestBody scoringDataDTO: ScoringDataDTO): CreditDTO?

}