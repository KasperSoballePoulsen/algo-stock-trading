package dk.ksp.algotrading.client

import dk.ksp.algotrading.entity.StockTrader
import dk.ksp.algotrading.enum.OrderType
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class BrokerClient {

    //TODO implement
    fun sendOrder(
        stockTrader: StockTrader,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ): Boolean {
        return true
    }
}