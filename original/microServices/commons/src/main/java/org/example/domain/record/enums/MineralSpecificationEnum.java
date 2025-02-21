package org.example.domain.record.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MineralSpecificationEnum {
  IRON("Iron , Fe", "mg", 8, 11, 8, 11),
  ZINC("Zinc , Zn", "mg", 11, 11, 8, 8),
  COPPER("Copper , Cu", "mg", 0.9, 2.3, 0.9, 2.3),
  MANGANESE("Manganese , Mn", "mg", 2.3, 2.3, 1.8, 1.8),
  IODINE("Iodine , I", "µg", 150, 150, 150, 150),
  SELENIUM("Selenium , Se", "µg", 55, 55, 55, 55),
  MOLYBDENUM("Molybdenum , Mo", "µg", 45, 45, 45, 50),
  CALCIUM("Calcium , Ca", "mg", 1000, 1300, 1000, 1300),
  PHOSPHORUS("Phosphorus , P", "mg", 700, 1250, 700, 1250),
  MAGNESIUM("Magnesium , Mg", "mg", 400, 420, 310, 320),
  SODIUM("Sodium , Na", "mg", 2300, 2300, 2300, 2300),
  POTASSIUM("Potassium , K", "mg", 3000, 3400, 2300, 2600);

  private final String name;
  private final String measure;
  private final double maleLowerBound;
  private final double maleUpperBound;
  private final double femaleLowerBound;
  private final double femaleUpperBound;
}