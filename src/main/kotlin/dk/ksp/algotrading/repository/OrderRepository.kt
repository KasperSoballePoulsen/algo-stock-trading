package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository: JpaRepository<Order, Long>