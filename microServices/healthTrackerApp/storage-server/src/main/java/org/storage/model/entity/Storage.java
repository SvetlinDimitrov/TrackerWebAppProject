package org.storage.model.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "storages")
public class Storage {

    @Id
    private String id;

    private BigDecimal consumedCalories;

    private String recordId;

    private String userId;

    private String name;

    private Map<String, Food> foods;
    private Map<String, Food> customFoods;

    public Storage(String name, String recordId, String userId) {
        this.name = name;
        this.recordId = recordId;
        consumedCalories = BigDecimal.ZERO;
        this.userId = userId;
        this.foods = new HashMap<>();
        this.customFoods = new HashMap<>();
    }
}
