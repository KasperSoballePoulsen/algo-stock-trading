package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order, Long>