package org.example.domain.food.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum NutrientType {
  MACRONUTRIENT(Macronutrient.class),
  MINERAL(Mineral.class),
  VITAMIN(Vitamin.class);

  private final Class<? extends Enum<?>> type;

  NutrientType(Class<? extends Enum<?>> type) {
    this.type = type;
  }

  @Getter
  public enum Macronutrient {
    CARBOHYDRATE("Carbohydrate"),
    PROTEIN("Protein"),
    FAT("Fat"),
    FIBER("Fiber"),
    TRANS_FAT("Trans Fat"),
    SATURATED_FAT("Saturated Fat"),
    SUGAR("Sugar"),
    POLYUNSATURATED_FAT("Polyunsaturated Fat"),
    MONOUNSATURATED_FAT("Monounsaturated Fat");

    private final String name;

    Macronutrient(String name) {
      this.name = name;
    }

  }

  @Getter
  public enum Mineral {
    CALCIUM("Calcium , Ca"),
    PHOSPHORUS("Phosphorus , P"),
    MAGNESIUM("Magnesium , Mg"),
    SODIUM("Sodium , Na"),
    POTASSIUM("Potassium , K"),
    IRON("Iron , Fe"),
    ZINC("Zinc , Zn"),
    COPPER("Copper , Cu"),
    MANGANESE("Manganese , Mn"),
    IODINE("Iodine , I"),
    SELENIUM("Selenium , Se"),
    MOLYBDENUM("Molybdenum , Mo");

    private final String name;

    Mineral(String name) {
      this.name = name;
    }

  }

  @Getter
  public enum Vitamin {
    VITAMIN_A("Vitamin A"),
    VITAMIN_D("Vitamin D (D2 + D3)"),
    VITAMIN_E("Vitamin E"),
    VITAMIN_K("Vitamin K"),
    VITAMIN_C("Vitamin C"),
    VITAMIN_B1("Vitamin B1 (Thiamin)"),
    VITAMIN_B2("Vitamin B2 (Riboflavin)"),
    VITAMIN_B3("Vitamin B3 (Niacin)"),
    VITAMIN_B5("Vitamin B5 (Pantothenic acid)"),
    VITAMIN_B6("Vitamin B6"),
    VITAMIN_B7("Vitamin B7 (Biotin)"),
    VITAMIN_B9("Vitamin B9 (Folate)"),
    VITAMIN_B12("Vitamin B12");

    private final String name;

    Vitamin(String name) {
      this.name = name;
    }

  }

  public static boolean isVitamin(String nutrientName) {
    return Arrays.stream(Vitamin.values())
        .anyMatch(vitamin -> vitamin.getName().equalsIgnoreCase(nutrientName));
  }

  public static boolean isMacro(String nutrientName) {
    return Arrays.stream(Macronutrient.values())
        .anyMatch(macro -> macro.getName().equalsIgnoreCase(nutrientName));
  }

  public static boolean isMineral(String nutrientName) {
    return Arrays.stream(Mineral.values())
        .anyMatch(mineral -> mineral.getName().equalsIgnoreCase(nutrientName));
  }
}