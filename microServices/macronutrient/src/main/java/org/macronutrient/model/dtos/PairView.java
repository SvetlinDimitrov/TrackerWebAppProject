package org.macronutrient.model.dtos;

import org.macronutrient.model.entity.Pair;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PairView {

    private String key;
    private String value;

    public PairView(Pair entity) {
        this.key = entity.getKey();
        this.value = entity.getValue();
    }

}
