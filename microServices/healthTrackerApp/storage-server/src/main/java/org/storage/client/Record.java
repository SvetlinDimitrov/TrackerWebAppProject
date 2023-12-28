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

    private String id;
    private String name;
    private BigDecimal dailyCalories = BigDecimal.ZERO;
    private String userId;
    private LocalDate date;

}
