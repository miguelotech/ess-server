package com.miguelotech.ess.controller;

import com.miguelotech.ess.service.DashboardService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*") // Permite que React/Angular se conecte sin bloqueos
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Helper record for date range
    private record DateRange(LocalDate start, LocalDate end) {}

    private DateRange getDateRange(String period) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now;

        switch (period) {
            case "7d":
                startDate = now.minusDays(7);
                break;
            case "30d":
                startDate = now.minusDays(30);
                break;
            case "quarter":
                startDate = now.minusMonths(3);
                break;
            case "year":
            default:
                startDate = now.minusYears(1);
                break;
        }
        
        return new DateRange(startDate, now);
    }

    // GET http://localhost:8080/api/dashboard/summary?period=7d
    @GetMapping("/summary")
    public Map<String, Object> getSummary(@RequestParam(defaultValue = "year") String period) {
        DateRange range = getDateRange(period);
        return dashboardService.getSummary(range.start(), range.end());
    }

    // GET http://localhost:8080/api/dashboard/chart-data?period=30d
    @GetMapping("/chart-data")
    public Map<String, BigDecimal> getChartData(@RequestParam(defaultValue = "year") String period) {
        DateRange range = getDateRange(period);
        return dashboardService.getSalesByMonth(range.start(), range.end());
    }

    // GET http://localhost:8080/api/dashboard/sales-by-category?period=quarter
    @GetMapping("/sales-by-category")
    public Map<String, BigDecimal> getSalesByCategory(@RequestParam(defaultValue = "year") String period) {
        DateRange range = getDateRange(period);
        return dashboardService.getSalesByCategory(range.start(), range.end());
    }

    // GET http://localhost:8080/api/dashboard/top-regions
    @GetMapping("/top-regions")
    public Map<String, BigDecimal> getTopRegions(@RequestParam(defaultValue = "year") String period) {
        DateRange range = getDateRange(period);
        return dashboardService.getSalesByRegion(range.start(), range.end());
    }

    // GET http://localhost:8080/api/dashboard/recent-sales
    @GetMapping("/recent-sales")
    public java.util.List<com.miguelotech.ess.model.Sale> getRecentSales(@RequestParam(defaultValue = "year") String period) {
        DateRange range = getDateRange(period);
        return dashboardService.getRecentSales(range.start(), range.end());
    }
}
