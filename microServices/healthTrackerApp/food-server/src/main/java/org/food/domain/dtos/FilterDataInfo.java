package org.food.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterDataInfo {
    private String nutrientName;
    private Boolean desc;
    private Integer limit;
    private Double max;
    private Double min;
}
