package dk.ksp.algotrading.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class StockPriceService {
    @Scheduled(initialDelay = 5_000)
    fun test() {
        println("DET VIRKER")
    }
}