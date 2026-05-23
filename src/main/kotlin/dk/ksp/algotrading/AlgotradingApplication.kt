package dk.ksp.algotrading

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class AlgotradingApplication

fun main(args: Array<String>) {
	runApplication<AlgotradingApplication>(*args)
}
