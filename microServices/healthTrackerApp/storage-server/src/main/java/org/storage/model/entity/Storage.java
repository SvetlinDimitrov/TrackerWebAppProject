package org.storage.model.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal consumedCalories;

    private Long recordId;

    private Long userId;

    private String name;

    @ElementCollection
    @CollectionTable(name = "storage_foods", joinColumns = @JoinColumn(name = "storage_id"))
    @MapKeyColumn(name = "food_name")
    @Column(name = "food")
    private Map<String, Food> foods;

    public Storage(String name, Long recordId , Long userId) {
        this.name = name;
        this.recordId = recordId;
        consumedCalories = BigDecimal.ZERO;
        this.userId = userId;
        this.foods = new HashMap<>();
    }
}
