package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.saxo.response.SaxoNetPosition
import dk.ksp.algotrading.entity.Holding
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.mapper.toHoldings
import dk.ksp.algotrading.repository.HoldingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HoldingService(
    private val holdingRepository: HoldingRepository
) {
    @Transactional
    fun replaceHoldings(
        account: TradingAccount,
        saxoNetPositions: List<SaxoNetPosition>
    ): List<Holding> {

        holdingRepository.deleteAllByTradingAccountId(account.id)

        return holdingRepository.saveAll(saxoNetPositions.toHoldings(account))
    }

    fun getHoldings(accountId: Long): List<Holding> = holdingRepository.findAllByAccountId(accountId)
}