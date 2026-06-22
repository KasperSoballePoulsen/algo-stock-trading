package dk.ksp.algotrading.config

import dk.ksp.algotrading.streaming.TradeMessageListener
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SaxoStreamingStartup(
    private val tradeMessageListener: TradeMessageListener
) {

    @EventListener(ApplicationReadyEvent::class)
    fun start() {
        tradeMessageListener.connect()
    }
}