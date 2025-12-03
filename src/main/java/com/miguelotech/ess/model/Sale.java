package com.miguelotech.ess.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
@Data // Lombok genera getters, setters, toString
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private BigDecimal amount;

    private String region; // Norte, Sur, Este, Oeste

    private String category; // Licencias, Consultoria, Soporte
}
