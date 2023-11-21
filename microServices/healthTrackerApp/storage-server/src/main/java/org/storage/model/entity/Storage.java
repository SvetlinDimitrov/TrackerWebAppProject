package org.storage.model.entity;

import java.math.BigDecimal;
import java.util.HashMap;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal consumedCalories;

    private Long recordId;

    private String name;

    @ElementCollection
    private HashMap<String, Food> foods;

    public Storage(String name , Long recordId) {
        this.name = name;
        this.recordId = recordId;
        this.foods = new HashMap<>();
    }
}
