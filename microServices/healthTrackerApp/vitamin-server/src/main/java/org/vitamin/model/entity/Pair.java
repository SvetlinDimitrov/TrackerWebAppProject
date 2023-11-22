package org.vitamin.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class Pair {

    private String key;
    private String value;

}
