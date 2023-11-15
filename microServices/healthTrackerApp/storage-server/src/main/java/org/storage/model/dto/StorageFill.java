package org.storage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageFill {

    private String foodName;
    private Long recordId;
    private String storageName;

}