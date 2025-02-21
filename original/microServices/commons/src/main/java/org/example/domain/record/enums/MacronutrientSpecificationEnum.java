package org.example.domain.record.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MacronutrientSpecificationEnum {
  CARBOHYDRATE("Carbohydrate", 0.5, 0.5),
  PROTEIN("Protein", 0.25, 0.15),
  FAT("Fat", 0.2, 0.35);

  private final String name;
  private final double activeState;
  private final double inactiveState;
}