package org.vitamin.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pair {

    private String key;
    private String value;

}