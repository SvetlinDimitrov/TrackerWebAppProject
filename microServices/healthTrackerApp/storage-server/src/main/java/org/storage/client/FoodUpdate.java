package org.storage.client;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodUpdate {

    private BigDecimal amount;
    private String foodName;
    
}
