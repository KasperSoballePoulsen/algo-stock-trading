package dk.ksp.algotrading.streaming

import com.fasterxml.jackson.databind.ObjectMapper
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.ByteBuffer


@Component
class SaxoStreamMessageParser(
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun parse(data: ByteBuffer): List<SaxoTradeMessageDTO> {
        val bytes = ByteArray(data.remaining())
        data.get(bytes)

        val jsonStartIndex = bytes.indexOfFirst { it == '['.code.toByte() }

        if (jsonStartIndex == -1) {
            logger.warn("Could not find JSON in Saxo binary message")
            return emptyList()
        }

        val json = String(
            bytes,
            jsonStartIndex,
            bytes.size - jsonStartIndex,
            Charsets.UTF_8
        )

        val root = objectMapper.readTree(json)

        return root
            .filterNot { it.path("ReferenceId").asText() == "_heartbeat" }
            .map { objectMapper.treeToValue(it, SaxoTradeMessageDTO::class.java) }
    }
}