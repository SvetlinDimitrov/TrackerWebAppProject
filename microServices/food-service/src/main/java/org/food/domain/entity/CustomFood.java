package org.food.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "foods")
public class CustomFood {

    @Id
    private Long id;
    //TODO: LATTER ADD A CUSTOM FOOD OPTION
}
