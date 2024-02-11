package org.example.conveyorkotlin.controller

import org.example.conveyorkotlin.api.ConveyorApi
import org.example.conveyorkotlin.dto.CreditDTO
import org.example.conveyorkotlin.dto.LoanApplicationRequestDTO
import org.example.conveyorkotlin.dto.LoanOfferDTO
import org.example.conveyorkotlin.dto.ScoringDataDTO
import org.example.conveyorkotlin.service.ConveyorService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ConveyorController
    (
    val conveyorService: ConveyorService
) : ConveyorApi {


    //    •	POST: /conveyor/offers - расчёт возможных условий кредита. Request - LoanApplicationRequestDTO, response - List<LoanOfferDTO>
    @PostMapping("/offers")
    override fun offers(loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO>? {
        return conveyorService.getOffers(loanApplicationRequestDTO)
    }

    //    •	POST: /conveyor/calculation - валидация присланных данных + скоринг данных + полный расчет параметров кредита. Request - ScoringDataDTO, response CreditDTO.
    @PostMapping("/calculation")
    override fun calculation(scoringDataDTO: ScoringDataDTO): CreditDTO? {
        return conveyorService.loanCalculation(scoringDataDTO)
    }


}