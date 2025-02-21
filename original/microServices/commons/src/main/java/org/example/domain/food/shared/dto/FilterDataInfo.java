package org.example.domain.food.shared.dto;

public record FilterDataInfo(
    String nutrientName,
    Boolean desc,
    Integer limit,
    Double max,
    Double min
) {
}