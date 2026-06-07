package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.BrokerClient
import dk.ksp.algotrading.dto.request.StockOrderResultDTO
import dk.ksp.algotrading.entity.StockTrader
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.repository.StockHoldingRepository
import dk.ksp.algotrading.repository.StockTraderRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class StockTradingService(
    private val stockTraderRepository: StockTraderRepository,
    private val stockHoldingRepository: StockHoldingRepository,
    private val orderExecutionService: OrderExecutionService,
    private val brokerClient: BrokerClient,
) {

    fun createOrder(
        stockTraderId: Long,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ): StockOrderResultDTO {
        val stockTrader = stockTraderRepository.findByIdAndDeletedAtIsNullWithTradingAccount(stockTraderId)
            ?: throw IllegalArgumentException("Trader not found")

        validateOrder(stockTrader, symbol, quantity, price, type)
        val isCompleted = brokerClient.sendOrder(stockTrader, symbol, quantity, price, type)
        if (isCompleted) {
            orderExecutionService.completeOrder(stockTrader.tradingAccount, symbol, quantity, price, type)
        }

        return StockOrderResultDTO(symbol, quantity, price, type, isCompleted)
    }

    private fun validateOrder(
        stockTrader: StockTrader,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ) {
        require(symbol.isNotBlank()) {
            "Symbol cannot be blank"
        }

        require(quantity > 0) {
            "Quantity must be greater than 0"
        }

        require(price > BigDecimal.ZERO) {
            "Price must be greater than 0"
        }

        if (type == OrderType.SELL) {
            val holding =
                stockHoldingRepository.findByTradingAccountAndSymbol(stockTrader.tradingAccount, symbol.uppercase())
                    ?: throw IllegalArgumentException("Cannot sell shares not owned")

            if (holding.quantity < quantity) {
                throw IllegalArgumentException("Cannot sell more shares than owned")
            }
        }
    }

}