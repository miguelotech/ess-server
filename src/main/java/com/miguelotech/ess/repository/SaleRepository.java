package com.miguelotech.ess.repository;

import com.miguelotech.ess.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Método mágico de JPA: Busca ventas entre dos fechas
    List<Sale> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Obtener las 5 ventas más recientes (global)
    List<Sale> findTop5ByOrderByDateDesc();

    // Obtener las 5 ventas más recientes dentro de un rango de fechas
    List<Sale> findTop5ByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);
}