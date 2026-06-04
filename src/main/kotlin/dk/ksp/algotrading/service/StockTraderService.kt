package dk.ksp.algotrading.service

import dk.ksp.algotrading.entity.StockTrader
import dk.ksp.algotrading.repository.StockTraderRepository
import org.springframework.stereotype.Service

@Service
class StockTraderService(
    private val stockTraderRepository: StockTraderRepository
) {
    fun createStockTrader(username: String) {
        val trader = StockTrader(username)
        stockTraderRepository.save(trader)
    }
}