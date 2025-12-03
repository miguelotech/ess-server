package com.miguelotech.ess.repository;

import com.miguelotech.ess.model.DailyMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyMetricRepository extends JpaRepository<DailyMetric, Long> {

    @Query("SELECT SUM(d.visitCount) FROM DailyMetric d WHERE d.date BETWEEN :startDate AND :endDate")
    Long sumVisitsByDateBetween(LocalDate startDate, LocalDate endDate);
}
