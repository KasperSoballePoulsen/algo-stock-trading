package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.dto.response.OrderDTO
import dk.ksp.algotrading.entity.Order
import dk.ksp.algotrading.enum.AssetType
import dk.ksp.algotrading.enum.BuySell
import dk.ksp.algotrading.enum.DurationType
import dk.ksp.algotrading.enum.Instrument
import dk.ksp.algotrading.enum.OrderInitiator
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.exception.BrokerRejectedException
import dk.ksp.algotrading.repository.OrderRepository
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class TradingService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val orderRepository: OrderRepository,
    private val saxoClient: SaxoClient,
) {
    private val logger = LoggerFactory.getLogger(javaClass)


    fun createOrder(
        symbol: String,
        quantity: Long,
        buySell: BuySell,
        orderType: OrderType,
        initiator: OrderInitiator,
        assetType: AssetType,
        durationType: DurationType
    ): OrderDTO {

        val tradingAccount = tradingAccountRepository.getTradingAccount()

        if (quantity <= 0) throw IllegalArgumentException("Quantity must be positive")
        if (symbol.isBlank()) throw IllegalArgumentException("Symbol is required")

        val normalizedSymbol = symbol.uppercase()

        val isManualOrder = when (initiator) {
            OrderInitiator.HUMAN -> true
            OrderInitiator.ALGORITHM -> false
        }
        val uic = Instrument.fromSymbol(normalizedSymbol)

        return try {
            val saxoOrder = saxoClient.sendOrder(
                tradingAccount.saxoAccountKey,
                quantity,
                buySell,
                orderType,
                isManualOrder,
                uic,
                assetType,
                durationType
            )

            val createdStatus = OrderStatus.PLACED

            orderRepository.save(
                Order(
                    symbol = normalizedSymbol,
                    uic = uic,
                    buySell = buySell,
                    quantity = quantity,
                    saxoOrderId = saxoOrder.orderId,
                    status = createdStatus,
                    orderType = orderType,
                    tradingAccount = tradingAccount,
                )
            )

            OrderDTO(normalizedSymbol, quantity, buySell, createdStatus)
        } catch (ex: BrokerRejectedException) {
            orderRepository.save(
                Order(
                    symbol = normalizedSymbol,
                    uic = uic,
                    buySell = buySell,
                    quantity = quantity,
                    status = OrderStatus.REJECTED,
                    orderType = orderType,
                    tradingAccount = tradingAccount,
                )
            )
            throw ex
        }
    }

    @Transactional
    fun updateOrder(
        saxoOrderId: String,
        orderStatus: OrderStatus,
        executionPrice: BigDecimal? = null
    ) {
        val order = orderRepository.findBySaxoOrderId(saxoOrderId)

        if (order == null) {
            logger.warn("Ignoring Saxo order update for unknown order id {}", saxoOrderId)
            return
        }

        executionPrice?.let {
            order.executedPrice = it
        }

        order.status = orderStatus
    }
}