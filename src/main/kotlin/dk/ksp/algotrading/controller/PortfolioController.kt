package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.request.AddStockRequestDTO
import dk.ksp.algotrading.service.PortfolioService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/traders/{username}/stocks")
class PortfolioController(
    private val portfolioService: PortfolioService
) {

    @PostMapping
    fun addStockToPortfolio(@PathVariable username: String, @RequestBody request: AddStockRequestDTO) {
        portfolioService.addStockToPortfolio(username, request.symbol)
    }

    @DeleteMapping("/{symbol}")
    fun removeStockFromPortfolio(@PathVariable username: String, @PathVariable symbol: String) {
//        portfolioService.removeStockFromPortfolio(username, symbol)
    }
}