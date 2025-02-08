package org.food.features.shared.dto;

public record NotCompleteFoodView(
    String id,
    String description,
    String foodClass
) {
}