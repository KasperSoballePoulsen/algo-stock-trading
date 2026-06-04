package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.CreateStockTraderRequestDTO
import dk.ksp.algotrading.service.StockTraderService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stock-traders")
class StockTraderController(
    private val stockTraderService: StockTraderService
) {

    @PostMapping
    fun createStockTrader(@RequestBody request: CreateStockTraderRequestDTO) {
        stockTraderService.createStockTrader(request.username)
    }

    @DeleteMapping("/{username}")
    fun deleteStockTrader(@PathVariable username: String) {
    }
}