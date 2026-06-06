package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockOrder
import org.springframework.data.jpa.repository.JpaRepository

interface StockOrderRepository: JpaRepository<StockOrder, Long>