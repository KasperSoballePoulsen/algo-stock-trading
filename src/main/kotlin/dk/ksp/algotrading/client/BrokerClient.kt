package dk.ksp.algotrading.client

import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.OrderType
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class BrokerClient {

    //TODO implement
    fun sendOrder(
        stockAccount: TradingAccount,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ): OrderStatus {
        return OrderStatus.FILLED
    }
}