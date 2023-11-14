package org.record.model.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private List<String> message;
}
