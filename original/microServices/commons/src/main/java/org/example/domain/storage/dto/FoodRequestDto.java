package org.example.domain.storage.dto;

import java.math.BigDecimal;
import java.util.List;

public record FoodRequestDto(
    String id,
    String description,
    String foodClass,
    String measurement,
    BigDecimal size
) {}