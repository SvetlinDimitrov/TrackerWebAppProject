package org.storage.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Calories {

    private String name;
    private BigDecimal amount;
    private String unit;

}
