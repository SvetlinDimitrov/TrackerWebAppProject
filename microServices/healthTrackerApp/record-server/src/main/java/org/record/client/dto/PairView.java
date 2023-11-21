package org.record.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PairView {

    private String key;
    private String value;

    public PairView(Pair entity) {
        this.key = entity.getKey();
        this.value = entity.getValue();
    }

}
