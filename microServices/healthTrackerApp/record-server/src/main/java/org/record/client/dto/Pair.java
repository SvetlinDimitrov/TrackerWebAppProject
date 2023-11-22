package org.record.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Pair {

    private String key;
    private String value;

}
