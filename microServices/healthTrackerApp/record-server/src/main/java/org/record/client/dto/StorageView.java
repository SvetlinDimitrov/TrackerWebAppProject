package org.record.client.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StorageView {

    private Long id;
    private BigDecimal consumedCalories;
    private List<Food> foods;
    private String name;

}
