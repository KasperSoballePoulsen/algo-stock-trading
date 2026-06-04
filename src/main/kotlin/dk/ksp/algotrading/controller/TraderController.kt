package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.CreateTraderRequestDTO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/traders")
class TraderController(
) {

    @PostMapping
    fun createTrader(@RequestBody request: CreateTraderRequestDTO) {
    }

    @DeleteMapping("/{username}")
    fun deleteTrader(@PathVariable username: String) {
    }
}