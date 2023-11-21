package org.record.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MacronutrientDto {

    private String name;
    private Double activeState;
    private Double inactiveState;

}
