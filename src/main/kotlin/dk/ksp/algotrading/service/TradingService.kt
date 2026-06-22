package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.BrokerClient
import dk.ksp.algotrading.dto.saxo.request.OrderDuration
import dk.ksp.algotrading.dto.response.SubmittedOrderDTO
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
import org.springframework.stereotype.Service

@Service
class TradingService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val orderRepository: OrderRepository,
    private val brokerClient: BrokerClient,
) {

    fun createOrder(
        symbol: String,
        quantity: Long,
        buySell: BuySell,
        orderType: OrderType,
        initiator: OrderInitiator,
        assetType: AssetType,
        durationType: DurationType
    ): SubmittedOrderDTO {

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
            val saxoOrder = brokerClient.sendOrder(
                tradingAccount.saxoAccountKey,
                quantity,
                buySell,
                orderType,
                isManualOrder,
                uic,
                assetType.saxoValue,
                OrderDuration(durationType.saxoValue)
            )

            val submittedStatus = OrderStatus.SUBMITTED

            orderRepository.save(
                Order(
                    symbol = normalizedSymbol,
                    uic = uic,
                    buySell = buySell,
                    quantity = quantity,
                    saxoOrderId = saxoOrder.orderId,
                    status = submittedStatus,
                    orderType = orderType,
                    tradingAccount = tradingAccount,
                )
            )

            SubmittedOrderDTO(symbol, quantity, buySell, submittedStatus)
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
}