package dk.ksp.algotrading.streaming

import com.fasterxml.jackson.databind.ObjectMapper
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import org.springframework.stereotype.Component
import java.nio.ByteBuffer


@Component
class SaxoStreamMessageParser(
    private val objectMapper: ObjectMapper
) {


    fun parse(data: ByteBuffer): List<SaxoTradeMessageDTO> {
        val bytes = ByteArray(data.remaining())
        data.get(bytes)

        val jsonStartIndex = bytes.indexOfFirst {
            it == '['.code.toByte() || it == '{'.code.toByte()
        }

        if (jsonStartIndex == -1) {
            println("Could not find JSON in binary message")
            return emptyList()
        }

        val json = String(
            bytes,
            jsonStartIndex,
            bytes.size - jsonStartIndex,
            Charsets.UTF_8
        )

        println("Saxo message: $json")

        val root = objectMapper.readTree(json)

        if (!root.isArray) {
            return emptyList()
        }

        return root
            .filter { it.has("AccountId") && it.has("MessageId") }
            .map { objectMapper.treeToValue(it, SaxoTradeMessageDTO::class.java) }
    }
}