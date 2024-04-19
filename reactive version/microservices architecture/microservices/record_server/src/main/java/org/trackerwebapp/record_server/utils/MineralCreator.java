package org.trackerwebapp.record_server.utils;

import org.trackerwebapp.shared_interfaces.domain.dto.NutritionIntakeView;
import org.trackerwebapp.shared_interfaces.domain.enums.AllowedNutrients;
import org.trackerwebapp.shared_interfaces.domain.enums.Gender;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class MineralCreator {
  /*
   data from: https://www.ncbi.nlm.nih.gov/books/NBK545442/table/appJ_tab3/?report=objectonly
   */

  public static final Set<String> allAllowedMinerals =
      Set.of(
          AllowedNutrients.Calcium_Ca.getNutrientName(),
          AllowedNutrients.Phosphorus_P.getNutrientName(),
          AllowedNutrients.Magnesium_Mg.getNutrientName(),
          AllowedNutrients.Sodium_Na.getNutrientName(),
          AllowedNutrients.Potassium_K.getNutrientName(),
          AllowedNutrients.Iron_Fe.getNutrientName(),
          AllowedNutrients.Zinc_Zn.getNutrientName(),
          AllowedNutrients.Copper_Cu.getNutrientName(),
          AllowedNutrients.Manganese_Mn.getNutrientName(),
          AllowedNutrients.Iodine_I.getNutrientName(),
          AllowedNutrients.Selenium_Se.getNutrientName(),
          AllowedNutrients.Molybdenum_Mo.getNutrientName()
      );

  public static Map<String, NutritionIntakeView> fillMinerals(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    fillCalcium(map , gender , age);
    fillChromium(map , gender , age);
    fillCopper(map , gender , age);
    fillFluoride(map , gender , age);
    fillIodine(map , gender , age);
    fillIron(map , gender , age);
    fillMagnesium(map , gender , age);
    fillManganese(map , gender , age);
    fillMolybdenum(map , gender , age);
    fillPhosphorus(map , gender , age);
    fillSelenium(map , gender , age);
    fillZinc(map , gender , age);
    fillPotassium(map , gender , age);
    fillSodium(map , gender , age);
    fillChloride(map , gender , age);
    return map;
  }

  private static void fillCalcium(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView calcium = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Calcium_Ca.getNutrientName())
        .measurement(AllowedNutrients.Copper_Cu.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(700);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(1000);
    } else {
      switch (gender) {
        case Gender.MALE -> {
          if (ageBetween(9, 18, age)) {
            dailyIntake = BigDecimal.valueOf(1300);
          } else if (ageBetween(19, 70, age)) {
            dailyIntake = BigDecimal.valueOf(1000);
          } else {
            dailyIntake = BigDecimal.valueOf(1200);
          }
        }
        case Gender.FEMALE -> {

          if (ageBetween(9, 18, age)) {
            dailyIntake = BigDecimal.valueOf(1300);
          } else if (ageBetween(19, 50, age)) {
            dailyIntake = BigDecimal.valueOf(1000);
          } else {
            dailyIntake = BigDecimal.valueOf(1200);
          }
        }
      }
    }
    calcium.setRecommendedIntake(dailyIntake);
    map.put(calcium.getName(), calcium);
  }

  private static void fillChromium(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView chromium = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Chromium_Cr.getNutrientName())
        .measurement(AllowedNutrients.Chromium_Cr.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(11);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(15);
    } else {
      switch (gender) {
        case Gender.MALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(25);
          } else if (ageBetween(14, 50, age)) {
            dailyIntake = BigDecimal.valueOf(35);
          } else {
            dailyIntake = BigDecimal.valueOf(30);
          }
        }
        case Gender.FEMALE -> {

          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(21);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(24);
          } else if (ageBetween(19, 50, age)) {
            dailyIntake = BigDecimal.valueOf(25);
          } else {
            dailyIntake = BigDecimal.valueOf(20);
          }
        }
      }
    }
    chromium.setRecommendedIntake(dailyIntake);
    map.put(chromium.getName(), chromium);
  }

  private static void fillCopper(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView copper = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Copper_Cu.getNutrientName())
        .measurement(AllowedNutrients.Copper_Cu.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(340);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(440);
    } else {
      switch (gender) {
        case Gender.MALE, Gender.FEMALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(700);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(890);
          } else {
            dailyIntake = BigDecimal.valueOf(900);
          }
        }
      }
    }
    copper.setRecommendedIntake(dailyIntake);
    map.put(copper.getName(), copper);
  }

  private static void fillFluoride(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView fluoride = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Fluoride.getNutrientName())
        .measurement(AllowedNutrients.Fluoride.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(0.7);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(1);
    } else {
      switch (gender) {
        case Gender.MALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(2);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(3);
          } else {
            dailyIntake = BigDecimal.valueOf(4);
          }
        }
        case Gender.FEMALE -> {

          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(2);
          } else {
            dailyIntake = BigDecimal.valueOf(3);
          }
        }
      }
    }
    fluoride.setRecommendedIntake(dailyIntake);
    map.put(fluoride.getName(), fluoride);
  }

  private static void fillIodine(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView iodine = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Iodine_I.getNutrientName())
        .measurement(AllowedNutrients.Iodine_I.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 8, age)) {
      dailyIntake = BigDecimal.valueOf(90);
    } else {
      switch (gender) {
        case Gender.MALE, Gender.FEMALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(120);
          } else {
            dailyIntake = BigDecimal.valueOf(150);
          }
        }
      }
    }
    iodine.setRecommendedIntake(dailyIntake);
    map.put(iodine.getName(), iodine);
  }

  private static void fillIron(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView iron = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Iron_Fe.getNutrientName())
        .measurement(AllowedNutrients.Iron_Fe.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(7);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(10);
    } else {
      switch (gender) {
        case Gender.MALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(8);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(11);
          } else {
            dailyIntake = BigDecimal.valueOf(8);
          }
        }
        case Gender.FEMALE -> {

          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(8);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(15);
          } else if (ageBetween(19, 50, age)) {
            dailyIntake = BigDecimal.valueOf(18);
          } else {
            dailyIntake = BigDecimal.valueOf(8);
          }
        }
      }
    }
    iron.setRecommendedIntake(dailyIntake);
    map.put(iron.getName(), iron);
  }

  private static void fillMagnesium(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView magnesium = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Magnesium_Mg.getNutrientName())
        .measurement(AllowedNutrients.Magnesium_Mg.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(80);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(130);
    } else {
      switch (gender) {
        case Gender.MALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(240);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(410);
          } else if (ageBetween(19, 30, age)) {
            dailyIntake = BigDecimal.valueOf(400);
          } else {
            dailyIntake = BigDecimal.valueOf(420);
          }
        }
        case Gender.FEMALE -> {

          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(240);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(360);
          } else if (ageBetween(19, 30, age)) {
            dailyIntake = BigDecimal.valueOf(310);
          } else {
            dailyIntake = BigDecimal.valueOf(320);
          }
        }
      }
    }
    magnesium.setRecommendedIntake(dailyIntake);
    map.put(magnesium.getName(), magnesium);
  }

  private static void fillManganese(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView manganese = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Manganese_Mn.getNutrientName())
        .measurement(AllowedNutrients.Manganese_Mn.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(1.2);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(1.5);
    } else {
      switch (gender) {
        case Gender.MALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(1.9);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(2.2);
          } else {
            dailyIntake = BigDecimal.valueOf(2.3);
          }
        }
        case Gender.FEMALE -> {
          if (ageBetween(9, 18, age)) {
            dailyIntake = BigDecimal.valueOf(1.6);
          } else {
            dailyIntake = BigDecimal.valueOf(1.8);
          }
        }
      }
    }
    manganese.setRecommendedIntake(dailyIntake);
    map.put(manganese.getName(), manganese);
  }

  private static void fillMolybdenum(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView molybdenum = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Molybdenum_Mo.getNutrientName())
        .measurement(AllowedNutrients.Molybdenum_Mo.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(17);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(22);
    } else {
      switch (gender) {
        case Gender.MALE, Gender.FEMALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(34);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(43);
          } else {
            dailyIntake = BigDecimal.valueOf(45);
          }
        }
      }
    }
    molybdenum.setRecommendedIntake(dailyIntake);
    map.put(molybdenum.getName(), molybdenum);
  }

  private static void fillPhosphorus(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView phosphorus = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Phosphorus_P.getNutrientName())
        .measurement(AllowedNutrients.Phosphorus_P.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(460);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(500);
    } else {
      switch (gender) {
        case Gender.MALE, Gender.FEMALE -> {
          if (ageBetween(9, 18, age)) {
            dailyIntake = BigDecimal.valueOf(1250);
          } else {
            dailyIntake = BigDecimal.valueOf(700);
          }
        }
      }
    }
    phosphorus.setRecommendedIntake(dailyIntake);
    map.put(phosphorus.getName(), phosphorus);
  }

  private static void fillSelenium(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView selenium = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Selenium_Se.getNutrientName())
        .measurement(AllowedNutrients.Selenium_Se.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(20);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(30);
    } else {
      switch (gender) {
        case Gender.MALE, Gender.FEMALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(40);
          } else {
            dailyIntake = BigDecimal.valueOf(55);
          }
        }
      }
    }
    selenium.setRecommendedIntake(dailyIntake);
    map.put(selenium.getName(), selenium);
  }

  private static void fillZinc(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView zinc = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Zinc_Zn.getNutrientName())
        .measurement(AllowedNutrients.Zinc_Zn.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(3);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(5);
    } else {
      switch (gender) {
        case Gender.MALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(8);
          } else {
            dailyIntake = BigDecimal.valueOf(11);
          }
        }
        case Gender.FEMALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(8);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(9);
          } else {
            dailyIntake = BigDecimal.valueOf(8);
          }
        }
      }
    }
    zinc.setRecommendedIntake(dailyIntake);
    map.put(zinc.getName(), zinc);
  }

  private static void fillPotassium(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView potassium = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Potassium_K.getNutrientName())
        .measurement(AllowedNutrients.Potassium_K.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(2000);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(2300);
    } else {
      switch (gender) {
        case Gender.MALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(2500);
          } else if (ageBetween(14, 18, age)) {
            dailyIntake = BigDecimal.valueOf(3000);
          } else {
            dailyIntake = BigDecimal.valueOf(3400);
          }
        }
        case Gender.FEMALE -> {
          if (ageBetween(9, 18, age)) {
            dailyIntake = BigDecimal.valueOf(2300);
          } else {
            dailyIntake = BigDecimal.valueOf(2600);
          }
        }
      }
    }
    potassium.setRecommendedIntake(dailyIntake);
    map.put(potassium.getName(), potassium);
  }

  private static void fillSodium(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView sodium = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Sodium_Na.getNutrientName())
        .measurement(AllowedNutrients.Sodium_Na.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(800);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(1000);
    } else {
      switch (gender) {
        case Gender.MALE, Gender.FEMALE -> {
          if (ageBetween(9, 13, age)) {
            dailyIntake = BigDecimal.valueOf(1200);
          } else {
            dailyIntake = BigDecimal.valueOf(1500);
          }
        }
      }
    }
    sodium.setRecommendedIntake(dailyIntake);
    map.put(sodium.getName(), sodium);
  }

  private static void fillChloride(Map<String, NutritionIntakeView> map, Gender gender, Integer age) {
    NutritionIntakeView chloride = NutritionIntakeView
        .builder()
        .name(AllowedNutrients.Chloride.getNutrientName())
        .measurement(AllowedNutrients.Chloride.getNutrientUnit())
        .dailyConsumed(BigDecimal.ZERO).build();

    BigDecimal dailyIntake = BigDecimal.ZERO;

    if (ageBetween(1, 3, age)) {
      dailyIntake = BigDecimal.valueOf(1.5);
    } else if (ageBetween(4, 8, age)) {
      dailyIntake = BigDecimal.valueOf(1.9);
    } else {
      switch (gender) {
        case Gender.MALE, Gender.FEMALE -> {
          if (ageBetween(9, 50, age)) {
            dailyIntake = BigDecimal.valueOf(2.3);
          } else if (ageBetween(51, 70, age)) {
            dailyIntake = BigDecimal.valueOf(2.0);
          } else {
            dailyIntake = BigDecimal.valueOf(1.8);
          }
        }
      }
    }
    chloride.setRecommendedIntake(dailyIntake);
    map.put(chloride.getName(), chloride);
  }

  private static boolean ageBetween(Integer minAge, Integer maxAge, Integer age) {
    return (age >= minAge && age <= maxAge);
  }

}
