package org.record.client.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VitaminDto {

    private String name;
    private String measure;
    private BigDecimal maleLowerBoundIntake;
    private BigDecimal maleHigherBoundIntake;
    private BigDecimal femaleLowerBoundIntake;
    private BigDecimal femaleHigherBoundIntake;
    
}
