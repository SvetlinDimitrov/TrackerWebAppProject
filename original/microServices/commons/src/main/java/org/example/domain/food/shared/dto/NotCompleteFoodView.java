package org.example.domain.food.shared.dto;

public record NotCompleteFoodView(
    String id,
    String description,
    String foodClass
) {
}