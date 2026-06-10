package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.CreateTraderDTO
import dk.ksp.algotrading.dto.response.TraderDTO
import dk.ksp.algotrading.dto.response.TraderWithTradingAccountsDTO
import dk.ksp.algotrading.service.TraderService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/traders")
class TraderController(
    private val traderService: TraderService
) {

    @PostMapping
    fun createTrader(@RequestBody request: CreateTraderDTO): TraderWithTradingAccountsDTO {
        return traderService.createTrader(request.username)
    }

    @DeleteMapping("/{traderId}")
    fun deleteTrader(@PathVariable traderId: Long) {
        traderService.deleteTrader(traderId)
    }

    @GetMapping("/{traderId}")
    fun getTrader(@PathVariable traderId: Long): TraderWithTradingAccountsDTO {
        return traderService.getTrader(traderId)
    }

    @GetMapping
    fun getTraders(): List<TraderDTO> {
        return traderService.getTraders()
    }

}