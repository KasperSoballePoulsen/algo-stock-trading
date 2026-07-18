package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.OrderRequestDTO
import dk.ksp.algotrading.dto.response.PortfolioDTO
import dk.ksp.algotrading.dto.response.OrderDTO
import dk.ksp.algotrading.service.TradingAccountService
import dk.ksp.algotrading.service.TradingService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trading-accounts")
class TradingAccountController(
    private val tradingService: TradingService,
    private val tradingAccountService: TradingAccountService
) {

    @PostMapping("/orders")
    fun createOrder(
        @Valid
        @RequestBody
        request: OrderRequestDTO
    ): ResponseEntity<OrderDTO> {
        val order = tradingService.createOrder(
            request.symbol,
            request.quantity,
            request.buySell,
            request.orderType,
            request.initiator,
            request.assetType,
            request.durationType
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(order)
    }

    @PostMapping("/portfolio/refresh")
    fun getPortfolioSynced(): PortfolioDTO {
        return tradingAccountService.getPortfolio(true)
    }

    @GetMapping("/portfolio")
    fun getPortfolio(): PortfolioDTO {
        return tradingAccountService.getPortfolio()
    }

}