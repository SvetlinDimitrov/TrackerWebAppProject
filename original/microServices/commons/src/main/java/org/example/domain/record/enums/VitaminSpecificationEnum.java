package org.example.domain.record.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VitaminSpecificationEnum {
  VITAMIN_A("Vitamin A", "µg", 600, 900, 600, 900),
  VITAMIN_D("Vitamin D (D2 + D3)", "µg", 10, 20, 10, 20),
  VITAMIN_E("Vitamin E", "mg", 15, 15, 15, 15),
  VITAMIN_K("Vitamin K", "µg", 120, 120, 90, 90),
  VITAMIN_C("Vitamin C", "mg", 90, 90, 75, 75),
  VITAMIN_B1("Vitamin B1 (Thiamin)", "mg", 1.2, 1.2, 1.1, 1.1),
  VITAMIN_B2("Vitamin B2 (Riboflavin)", "mg", 1.3, 1.3, 1.3, 1.3),
  VITAMIN_B3("Vitamin B3 (Niacin)", "mg", 16, 16, 14, 14),
  VITAMIN_B5("Vitamin B5 (Pantothenic acid)", "mg", 5, 5, 5, 5),
  VITAMIN_B6("Vitamin B6", "mg", 1.3, 1.3, 1.3, 1.3),
  VITAMIN_B7("Vitamin B7 (Biotin)", "µg", 30, 30, 30, 30),
  VITAMIN_B9("Vitamin B9 (Folate)", "µg", 400, 400, 400, 400),
  VITAMIN_B12("Vitamin B12", "µg", 2.4, 2.4, 2.4, 2.4);

  private final String name;
  private final String measure;
  private final double maleLowerBound;
  private final double maleUpperBound;
  private final double femaleLowerBound;
  private final double femaleUpperBound;
}