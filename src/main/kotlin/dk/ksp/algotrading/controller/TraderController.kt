package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.response.TraderWithTradingAccountDTO
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

    @GetMapping
    fun getTrader(): TraderWithTradingAccountDTO {
        return traderService.getTrader()
    }

}