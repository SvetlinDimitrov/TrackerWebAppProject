package org.storage.model.dto;

import java.math.BigDecimal;
import java.util.List;

import org.storage.model.entity.Food;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StorageView {

    private Long id;
    private Long recordId;
    private String name;
    private BigDecimal consumedCalories;
    private List<Food> foods;

}
