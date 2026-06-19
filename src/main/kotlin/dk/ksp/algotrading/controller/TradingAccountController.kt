package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.AccountTransactionRequestDTO
import dk.ksp.algotrading.dto.request.OrderRequestDTO
import dk.ksp.algotrading.dto.response.OrderDTO
import dk.ksp.algotrading.dto.response.TradingAccountWithHoldingsDTO
import dk.ksp.algotrading.service.AccountTransactionService
import dk.ksp.algotrading.service.TradingAccountService
import dk.ksp.algotrading.service.TradingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trading-accounts/{tradingAccountId}")
class TradingAccountController(
    private val accountTransactionService: AccountTransactionService,
    private val tradingService: TradingService,
    private val tradingAccountService: TradingAccountService,
) {

    @PostMapping("/orders")
    fun createOrder(
        @PathVariable tradingAccountId: Long,
        @RequestBody request: OrderRequestDTO
    ): OrderDTO {
        return tradingService.createOrder(
            tradingAccountId,
            request.symbol,
            request.quantity,
            request.buySell,
            request.orderType,
            request.initiator
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
    fun getTradingAccount(@PathVariable tradingAccountId: Long): TradingAccountWithHoldingsDTO {
        return tradingAccountService.getTradingAccount(tradingAccountId)
    }

}