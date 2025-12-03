package com.miguelotech.ess.config;

import com.miguelotech.ess.model.DailyMetric;
import com.miguelotech.ess.repository.DailyMetricRepository;
import net.datafaker.Faker;
import com.miguelotech.ess.model.Sale;
import com.miguelotech.ess.repository.SaleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Profile;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final SaleRepository saleRepository;
    private final DailyMetricRepository dailyMetricRepository;
    private final Faker faker;

    public DataSeeder(SaleRepository saleRepository, DailyMetricRepository dailyMetricRepository) {
        this.saleRepository = saleRepository;
        this.dailyMetricRepository = dailyMetricRepository;
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo ejecuta si la base de datos está vacía
        if (saleRepository.count() == 0) {
            System.out.println("--> SEEDER: Iniciando generación de datos...");
            List<Sale> sales = new ArrayList<>();

            // Generar datos para los últimos 12 meses (Aumentado a 3000 registros)
            for (int i = 0; i < 1000; i++) {
                Sale sale = new Sale();

                // Fecha aleatoria en el último año
                sale.setDate(faker.date().past(365, TimeUnit.DAYS)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                // Monto aleatorio entre 100 y 5000 (Simulando tendencia)
                double basePrice = 100 + Math.random() * 4900;
                sale.setAmount(BigDecimal.valueOf(basePrice));

                // Datos categóricos
                // Regiones de Perú
                String[] regions = {
                    "Amazonas", "Ancash", "Apurimac", "Arequipa", "Ayacucho", "Cajamarca", "Callao",
                    "Cusco", "Huancavelica", "Huanuco", "Ica", "Junin", "La Libertad", "Lambayeque",
                    "Lima", "Loreto", "Madre de Dios", "Moquegua", "Pasco", "Piura", "Puno",
                    "San Martin", "Tacna", "Tumbes", "Ucayali"
                };
                sale.setRegion(regions[faker.random().nextInt(regions.length)]);
                String[] categories = {"Fertilizantes", "Semillas", "Herbicidas", "Maquinaria", "Pesticidas", "Suelos", "Riego"};
                sale.setCategory(categories[faker.random().nextInt(categories.length)]);

                sales.add(sale);
            }

            saleRepository.saveAll(sales);
            System.out.println("--> SEEDER: " + sales.size() + " ventas generadas.");
            
            // Generar métricas diarias (Visitas)
            List<DailyMetric> metrics = new ArrayList<>();
            LocalDate today = LocalDate.now();
            for (int i = 0; i <= 365; i++) {
                LocalDate date = today.minusDays(i);
                DailyMetric metric = new DailyMetric();
                metric.setDate(date);
                
                // Visitas aleatorias entre 50 y 500
                metric.setVisitCount((long) (50 + Math.random() * 450));
                
                metrics.add(metric);
            }
            dailyMetricRepository.saveAll(metrics);
            System.out.println("--> SEEDER: Métricas diarias de visitas generadas.");
        }
    }
}