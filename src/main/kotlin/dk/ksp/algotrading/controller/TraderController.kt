package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.response.TraderDTO
import dk.ksp.algotrading.service.TraderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/traders")
class TraderController(
    private val traderService: TraderService
) {

    @GetMapping
    fun getTrader(): TraderDTO {
        return traderService.getTrader()
    }

}