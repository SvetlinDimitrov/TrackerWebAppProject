package org.storage.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "storages")
@Data
@NoArgsConstructor
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Long recordId;

    @ElementCollection
    private List<String> foodNames;

    public Storage(String name, Long recordId) {
        this.name = name;
        this.recordId = recordId;
        this.foodNames = new ArrayList<>();
    }
}
