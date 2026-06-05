package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.AddStockHoldingDTO
import dk.ksp.algotrading.dto.response.StockTraderWithPortfolioDTO
import dk.ksp.algotrading.service.PortfolioService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stock-traders/{username}/stocks")
class PortfolioController(
    private val portfolioService: PortfolioService
) {

    @PostMapping
    fun addStockHolding(
        @PathVariable username: String,
        @RequestBody request: AddStockHoldingDTO
    ): StockTraderWithPortfolioDTO {
        return portfolioService.addStockHolding(username, request.symbol, request.quantity)
    }

    @DeleteMapping("/{symbol}")
    fun removeStockHolding(
        @PathVariable username: String,
        @PathVariable symbol: String,
        @RequestParam quantity: Long
    ): StockTraderWithPortfolioDTO {
        return portfolioService.removeStockHolding(username, symbol, quantity)
    }
}