package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.StockOrderRequestDTO
import dk.ksp.algotrading.dto.request.StockOrderResultDTO
import dk.ksp.algotrading.service.StockTradingService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stock-traders/{stockTraderId}/orders")
class StockTradingController(
    private val stockTradingService: StockTradingService
) {

    @PostMapping
    fun createStockOrder(
        @PathVariable stockTraderId: Long,
        @RequestBody request: StockOrderRequestDTO
    ): StockOrderResultDTO {
        return stockTradingService.createOrder(stockTraderId, request.symbol, request.quantity, request.price, request.type)
    }


}