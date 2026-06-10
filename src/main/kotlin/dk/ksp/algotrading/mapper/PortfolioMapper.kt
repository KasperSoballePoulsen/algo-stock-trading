package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.HoldingDTO
import dk.ksp.algotrading.entity.Holding

fun List<Holding>.toHoldingsDTO() = map { it.toHoldingDTO() }


fun Holding.toHoldingDTO() = HoldingDTO(symbol, quantity)