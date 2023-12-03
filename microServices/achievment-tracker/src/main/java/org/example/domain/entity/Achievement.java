package org.example.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private BigDecimal progress;
    private BigDecimal goal;
    private String measurement;
    private LocalDate startDate;

    private LinkedList<BigDecimal> dailyProgress = new LinkedList<>();
    private LinkedList<BigDecimal> weaklyProgress = new LinkedList<>();
    private LinkedList<BigDecimal> monthlyProgress = new LinkedList<>();
    private LinkedList<BigDecimal> yearlyProgress = new LinkedList<>();
}
