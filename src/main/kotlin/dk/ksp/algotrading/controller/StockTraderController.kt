package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.CreateStockTraderDTO
import dk.ksp.algotrading.dto.response.StockTraderDTO
import dk.ksp.algotrading.dto.response.StockTraderWithTradingAccountDTO
import dk.ksp.algotrading.service.StockTraderService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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
    fun createStockTrader(@RequestBody request: CreateStockTraderDTO): StockTraderWithTradingAccountDTO {
        return stockTraderService.createStockTrader(request.username)
    }

    @DeleteMapping("/{stockTraderId}")
    fun deleteStockTrader(@PathVariable stockTraderId: Long) {
        stockTraderService.deleteTrader(stockTraderId)
    }

    @GetMapping("/{stockTraderId}")
    fun getStockTrader(@PathVariable stockTraderId: Long): StockTraderWithTradingAccountDTO {
        return stockTraderService.getStockTrader(stockTraderId)
    }

    @GetMapping
    fun getStockTraders(): List<StockTraderDTO> {
        return stockTraderService.getStockTraders()
    }

}