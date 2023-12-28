package org.record.client.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StorageView {

    private String id;
    private BigDecimal consumedCalories;
    private List<Food> foods;
    private String name;

}
