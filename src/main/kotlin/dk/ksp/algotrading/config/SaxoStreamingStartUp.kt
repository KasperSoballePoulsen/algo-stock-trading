package dk.ksp.algotrading.config

import dk.ksp.algotrading.service.StreamingService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SaxoStreamingStartup(
    private val tradeMessageStreamingService: StreamingService
) {

    @EventListener(ApplicationReadyEvent::class)
    fun start() {
        tradeMessageStreamingService.connect()
    }
}