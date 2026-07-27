package dk.ksp.algotrading.controller

import dk.ksp.algotrading.dto.response.DashboardDTO
import dk.ksp.algotrading.service.DashboardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DashBoardController(
    private val dashboardService: DashboardService
) {

    @GetMapping("/Dashboard")
    fun getDashBoard(): DashboardDTO =
        dashboardService.getDashboard()

}