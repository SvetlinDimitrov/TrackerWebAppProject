package org.food.domain.dtos.foodView.storageFoodView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BrandedInfoView {
    private String ingredients;
    private String brandOwner;
    private String marketCountry;
    private BigDecimal servingSize;
    private String servingSizeUnit;
}
