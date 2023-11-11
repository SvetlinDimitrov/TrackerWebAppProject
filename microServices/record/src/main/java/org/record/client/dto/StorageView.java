package org.record.client.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StorageView {
    private Long id;
    private Long recordId;
    private List<String> foods;
    private String name;
}
