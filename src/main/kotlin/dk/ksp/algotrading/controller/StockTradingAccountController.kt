package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.AccountTransactionRequestDTO
import dk.ksp.algotrading.dto.request.StockOrderRequestDTO
import dk.ksp.algotrading.dto.response.StockOrderDTO
import dk.ksp.algotrading.dto.response.StockTradingAccountWithHoldingsDTO
import dk.ksp.algotrading.service.AccountTransactionService
import dk.ksp.algotrading.service.StockTradingAccountService
import dk.ksp.algotrading.service.StockTradingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trading-accounts/{tradingAccountId}")
class StockTradingAccountController(
    private val accountTransactionService: AccountTransactionService,
    private val stockTradingService: StockTradingService,
    private val stockTradingAccountService: StockTradingAccountService,
) {

    @PostMapping("/orders")
    fun createStockOrder(
        @PathVariable tradingAccountId: Long,
        @RequestBody request: StockOrderRequestDTO
    ): StockOrderDTO {
        return stockTradingService.createOrder(
            tradingAccountId,
            request.symbol,
            request.quantity,
            request.price,
            request.type
        )
    }

    @PostMapping("/account-transactions")
    fun createManualAccountTransaction(
        @PathVariable tradingAccountId: Long,
        @RequestBody request: AccountTransactionRequestDTO
    ) {
        accountTransactionService.createManualAccountTransaction(tradingAccountId, request.type, request.amount)
    }

    @GetMapping
    fun getStockTradingAccount(@PathVariable tradingAccountId: Long): StockTradingAccountWithHoldingsDTO {
        return stockTradingAccountService.getStockTradingAccount(tradingAccountId)
    }

}