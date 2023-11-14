package org.record.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageDeletion {

    private Long storageId;
    private Long recordId;

}
