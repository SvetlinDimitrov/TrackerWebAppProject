package org.storage.client;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Record {

    private Long id;
    private String name;
    private BigDecimal dailyCalories = BigDecimal.ZERO;
    private Long userId;
    private LocalDate date;

}
