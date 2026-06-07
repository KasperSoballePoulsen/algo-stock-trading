package dk.ksp.algotrading.dto.request

import dk.ksp.algotrading.enum.AccountTransactionType
import java.math.BigDecimal

data class AccountTransactionRequestDTO(
    val type: AccountTransactionType,
    val amount: BigDecimal
)