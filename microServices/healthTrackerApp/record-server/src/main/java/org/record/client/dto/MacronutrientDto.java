package org.record.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MacronutrientDto {

    private String name;
    private Double activeState;
    private Double inactiveState;

}
