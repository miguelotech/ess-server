package com.miguelotech.ess.service;

import com.miguelotech.ess.model.Sale;
import com.miguelotech.ess.repository.SaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DashboardServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTotalRevenueWithinLimitReturnsSummary() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        // Sales that sum up to less than 1 million
        List<Sale> sales = Arrays.asList(
                new Sale(3L, LocalDate.now(), BigDecimal.valueOf(300_000), "Norte", "Licencias"),
                new Sale(4L, LocalDate.now(), BigDecimal.valueOf(400_000), "Sur", "Consultoria")
        );

        when(saleRepository.findByDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(sales);

        Assertions.assertDoesNotThrow(() -> {
            dashboardService.getSummary(startDate, endDate);
        });
    }

    @Test
    void testTotalRevenueEqualsLimitReturnsSummary() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        // Sales that sum up to exactly 1 million
        List<Sale> sales = Arrays.asList(
                new Sale(5L, LocalDate.now(), BigDecimal.valueOf(500_000), "Norte", "Licencias"),
                new Sale(6L, LocalDate.now(), BigDecimal.valueOf(500_000), "Sur", "Consultoria")
        );

        when(saleRepository.findByDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(sales);

        Assertions.assertDoesNotThrow(() -> {
            dashboardService.getSummary(startDate, endDate);
        });
    }

    @Test
    void testNoSalesReturnsSummary() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(saleRepository.findByDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.emptyList());

        Assertions.assertDoesNotThrow(() -> {
            dashboardService.getSummary(startDate, endDate);
        });
    }
}
