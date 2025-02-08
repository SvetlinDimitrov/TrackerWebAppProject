package org.food.features.embedded.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class BrandedInfo {

    private String ingredients;
    private String brandOwner;
    private String marketCountry;
    private BigDecimal servingSize;
    private String servingSizeUnit;
}
