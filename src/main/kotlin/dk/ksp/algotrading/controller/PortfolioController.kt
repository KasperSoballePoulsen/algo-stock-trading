package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.StockOrderDTO
import dk.ksp.algotrading.dto.response.StockTraderWithPortfolioDTO
import dk.ksp.algotrading.service.PortfolioService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stock-traders/{username}/orders")
class PortfolioController(
    private val portfolioService: PortfolioService
) {

    @PostMapping
    fun createOrder(
        @PathVariable username: String,
        @RequestBody request: StockOrderDTO
    ): StockOrderDTO {
        return portfolioService.createOrder(username, request.symbol, request.quantity, request.price, request.type)
    }


}