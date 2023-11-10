package org.record.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StorageView {
    private Long id;
    private Long recordId;
    private String name;
}
