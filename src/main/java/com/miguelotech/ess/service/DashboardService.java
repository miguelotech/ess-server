package com.miguelotech.ess.service;

import com.miguelotech.ess.model.Sale;
import com.miguelotech.ess.repository.DailyMetricRepository;
import com.miguelotech.ess.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final SaleRepository saleRepository;
    private final DailyMetricRepository dailyMetricRepository;

    public DashboardService(SaleRepository saleRepository, DailyMetricRepository dailyMetricRepository) {
        this.saleRepository = saleRepository;
        this.dailyMetricRepository = dailyMetricRepository;
    }

    // 1. KPI: Total de Ventas
    public Map<String, Object> getSummary(LocalDate start, LocalDate end) {
        List<Sale> sales = saleRepository.findByDateBetween(start, end);

        BigDecimal totalAmount = sales.stream()
                .map(Sale::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int transactionCount = sales.size();
        
        // Obtener visitas reales de la base de datos
        Long visitCount = dailyMetricRepository.sumVisitsByDateBetween(start, end);
        if (visitCount == null) visitCount = 0L;

        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", totalAmount);
        response.put("transactionCount", transactionCount);
        response.put("visitCount", visitCount);
        return response;
    }

    // 2. Gráfico: Ventas por Mes (Para gráfico de barras/líneas)
    public Map<String, BigDecimal> getSalesByMonth(LocalDate start, LocalDate end) {
        List<Sale> sales = saleRepository.findByDateBetween(start, end);

        // Agrupar por Mes y Sumar
        return sales.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getDate().getMonth().toString(), // Clave: Nombre del mes
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Sale::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    // 3. Gráfico: Ventas por Categoría (Pie Chart)
    public Map<String, BigDecimal> getSalesByCategory(LocalDate start, LocalDate end) {
        List<Sale> sales = saleRepository.findByDateBetween(start, end);

        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Sale::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    // 4. Gráfico: Top Regiones (Bar Chart)
    public Map<String, BigDecimal> getSalesByRegion(LocalDate start, LocalDate end) {
        List<Sale> sales = saleRepository.findByDateBetween(start, end);

        // Agrupar por región y sumar montos
        Map<String, BigDecimal> regionSales = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getRegion,
                        Collectors.reducing(BigDecimal.ZERO, Sale::getAmount, BigDecimal::add)
                ));

        // Ordenar y limitar a top 5 (opcional, pero recomendado para UI)
        return regionSales.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        java.util.LinkedHashMap::new // Mantener orden de inserción
                ));
    }

    // 5. Tabla: Últimas Transacciones
    public List<Sale> getRecentSales(LocalDate start, LocalDate end) {
        return saleRepository.findTop5ByDateBetweenOrderByDateDesc(start, end);
    }
}