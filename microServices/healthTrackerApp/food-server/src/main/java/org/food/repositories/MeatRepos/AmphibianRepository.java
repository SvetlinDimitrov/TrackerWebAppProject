package org.food.repositories.MeatRepos;

import java.math.BigDecimal;

import org.food.domain.dtos.Food;
import org.food.repositories.FoodRepository;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AmphibianRepository {

    private final FoodRepository foodRepository;

    @PostConstruct
    private void initData() {
        Food frogLegs = Food.builder()
                .name("Frog Legs")
                .calories(new BigDecimal("70"))
                .A(new BigDecimal("50"))
                .D(new BigDecimal("0"))
                .E(new BigDecimal("1"))
                .K(new BigDecimal("0.1"))
                .C(new BigDecimal("0"))
                .B1(new BigDecimal("0.1"))
                .B2(new BigDecimal("0.2"))
                .B3(new BigDecimal("1"))
                .B5(new BigDecimal("0.1"))
                .B6(new BigDecimal("0.1"))
                .B7(new BigDecimal("0"))
                .B9(new BigDecimal("0"))
                .B12(new BigDecimal("0"))
                .Calcium(new BigDecimal("10"))
                .Phosphorus(new BigDecimal("200"))
                .Magnesium(new BigDecimal("20"))
                .Sodium(new BigDecimal("75"))
                .Potassium(new BigDecimal("180"))
                .Chloride(new BigDecimal("0"))
                .Iron(new BigDecimal("2"))
                .Zinc(new BigDecimal("1.5"))
                .Copper(new BigDecimal("0.1"))
                .Manganese(new BigDecimal("0.1"))
                .Iodine(new BigDecimal("0"))
                .Selenium(new BigDecimal("22"))
                .Fluoride(new BigDecimal("0"))
                .Chromium(new BigDecimal("0"))
                .Molybdenum(new BigDecimal("0"))
                .Carbohydrates(new BigDecimal("0"))
                .Protein(new BigDecimal("16"))
                .Fat(new BigDecimal("0.5"))
                .Fiber(new BigDecimal("0"))
                .TransFat(new BigDecimal("0"))
                .SaturatedFat(new BigDecimal("0"))
                .Sugar(new BigDecimal("0"))
                .PolyunsaturatedFat(new BigDecimal("0.2"))
                .MonounsaturatedFat(new BigDecimal("0.1"))
                .build();

        foodRepository.addFood(frogLegs);
        Food frogSkin = Food.builder()
                .name("Frog Skin")
                .calories(new BigDecimal("45"))
                .A(new BigDecimal("20"))
                .D(new BigDecimal("0"))
                .E(new BigDecimal("0.5"))
                .K(new BigDecimal("0.1"))
                .C(new BigDecimal("0"))
                .B1(new BigDecimal("0.1"))
                .B2(new BigDecimal("0.1"))
                .B3(new BigDecimal("0.5"))
                .B5(new BigDecimal("0.1"))
                .B6(new BigDecimal("0.1"))
                .B7(new BigDecimal("0"))
                .B9(new BigDecimal("0"))
                .B12(new BigDecimal("0"))
                .Calcium(new BigDecimal("10"))
                .Phosphorus(new BigDecimal("150"))
                .Magnesium(new BigDecimal("15"))
                .Sodium(new BigDecimal("60"))
                .Potassium(new BigDecimal("150"))
                .Chloride(new BigDecimal("0"))
                .Iron(new BigDecimal("0.5"))
                .Zinc(new BigDecimal("1"))
                .Copper(new BigDecimal("0.1"))
                .Manganese(new BigDecimal("0.1"))
                .Iodine(new BigDecimal("0"))
                .Selenium(new BigDecimal("20"))
                .Fluoride(new BigDecimal("0"))
                .Chromium(new BigDecimal("0"))
                .Molybdenum(new BigDecimal("0"))
                .Carbohydrates(new BigDecimal("0"))
                .Protein(new BigDecimal("8"))
                .Fat(new BigDecimal("3"))
                .Fiber(new BigDecimal("0"))
                .TransFat(new BigDecimal("0"))
                .SaturatedFat(new BigDecimal("1"))
                .Sugar(new BigDecimal("0"))
                .PolyunsaturatedFat(new BigDecimal("0.5"))
                .MonounsaturatedFat(new BigDecimal("1"))
                .build();

        foodRepository.addFood(frogSkin);
        Food frogLiver = Food.builder()
                .name("Frog Liver")
                .calories(new BigDecimal("60"))
                .A(new BigDecimal("800"))
                .D(new BigDecimal("1"))
                .E(new BigDecimal("0.8"))
                .K(new BigDecimal("0.1"))
                .C(new BigDecimal("0"))
                .B1(new BigDecimal("0.2"))
                .B2(new BigDecimal("0.5"))
                .B3(new BigDecimal("2"))
                .B5(new BigDecimal("0.1"))
                .B6(new BigDecimal("0.1"))
                .B7(new BigDecimal("0"))
                .B9(new BigDecimal("0"))
                .B12(new BigDecimal("0.2"))
                .Calcium(new BigDecimal("10"))
                .Phosphorus(new BigDecimal("150"))
                .Magnesium(new BigDecimal("15"))
                .Sodium(new BigDecimal("60"))
                .Potassium(new BigDecimal("150"))
                .Chloride(new BigDecimal("0"))
                .Iron(new BigDecimal("3"))
                .Zinc(new BigDecimal("2"))
                .Copper(new BigDecimal("0.2"))
                .Manganese(new BigDecimal("0.1"))
                .Iodine(new BigDecimal("0"))
                .Selenium(new BigDecimal("20"))
                .Fluoride(new BigDecimal("0"))
                .Chromium(new BigDecimal("0"))
                .Molybdenum(new BigDecimal("0"))
                .Carbohydrates(new BigDecimal("0"))
                .Protein(new BigDecimal("8"))
                .Fat(new BigDecimal("3"))
                .Fiber(new BigDecimal("0"))
                .TransFat(new BigDecimal("0"))
                .SaturatedFat(new BigDecimal("1"))
                .Sugar(new BigDecimal("0"))
                .PolyunsaturatedFat(new BigDecimal("0.5"))
                .MonounsaturatedFat(new BigDecimal("1.5"))
                .build();

        foodRepository.addFood(frogLiver);
        Food frogHeart = Food.builder()
                .name("Frog Heart")
                .calories(new BigDecimal("50"))
                .A(new BigDecimal("30"))
                .D(new BigDecimal("0"))
                .E(new BigDecimal("0.8"))
                .K(new BigDecimal("0.1"))
                .C(new BigDecimal("0"))
                .B1(new BigDecimal("0.1"))
                .B2(new BigDecimal("0.2"))
                .B3(new BigDecimal("1"))
                .B5(new BigDecimal("0.1"))
                .B6(new BigDecimal("0.1"))
                .B7(new BigDecimal("0"))
                .B9(new BigDecimal("0"))
                .B12(new BigDecimal("0.1"))
                .Calcium(new BigDecimal("10"))
                .Phosphorus(new BigDecimal("150"))
                .Magnesium(new BigDecimal("15"))
                .Sodium(new BigDecimal("60"))
                .Potassium(new BigDecimal("150"))
                .Chloride(new BigDecimal("0"))
                .Iron(new BigDecimal("2.5"))
                .Zinc(new BigDecimal("1.5"))
                .Copper(new BigDecimal("0.2"))
                .Manganese(new BigDecimal("0.1"))
                .Iodine(new BigDecimal("0"))
                .Selenium(new BigDecimal("20"))
                .Fluoride(new BigDecimal("0"))
                .Chromium(new BigDecimal("0"))
                .Molybdenum(new BigDecimal("0"))
                .Carbohydrates(new BigDecimal("0"))
                .Protein(new BigDecimal("9"))
                .Fat(new BigDecimal("2"))
                .Fiber(new BigDecimal("0"))
                .TransFat(new BigDecimal("0"))
                .SaturatedFat(new BigDecimal("1"))
                .Sugar(new BigDecimal("0"))
                .PolyunsaturatedFat(new BigDecimal("0.5"))
                .MonounsaturatedFat(new BigDecimal("0.5"))
                .build();

        foodRepository.addFood(frogHeart);
        Food frogKidneys = Food.builder()
                .name("Frog Kidneys")
                .calories(new BigDecimal("45"))
                .A(new BigDecimal("30"))
                .D(new BigDecimal("0"))
                .E(new BigDecimal("0.5"))
                .K(new BigDecimal("0.1"))
                .C(new BigDecimal("0"))
                .B1(new BigDecimal("0.1"))
                .B2(new BigDecimal("0.2"))
                .B3(new BigDecimal("1"))
                .B5(new BigDecimal("0.1"))
                .B6(new BigDecimal("0.1"))
                .B7(new BigDecimal("0"))
                .B9(new BigDecimal("0"))
                .B12(new BigDecimal("0.1"))
                .Calcium(new BigDecimal("10"))
                .Phosphorus(new BigDecimal("150"))
                .Magnesium(new BigDecimal("15"))
                .Sodium(new BigDecimal("60"))
                .Potassium(new BigDecimal("150"))
                .Chloride(new BigDecimal("0"))
                .Iron(new BigDecimal("2.5"))
                .Zinc(new BigDecimal("1.5"))
                .Copper(new BigDecimal("0.2"))
                .Manganese(new BigDecimal("0.1"))
                .Iodine(new BigDecimal("0"))
                .Selenium(new BigDecimal("20"))
                .Fluoride(new BigDecimal("0"))
                .Chromium(new BigDecimal("0"))
                .Molybdenum(new BigDecimal("0"))
                .Carbohydrates(new BigDecimal("0"))
                .Protein(new BigDecimal("8"))
                .Fat(new BigDecimal("2"))
                .Fiber(new BigDecimal("0"))
                .TransFat(new BigDecimal("0"))
                .SaturatedFat(new BigDecimal("1"))
                .Sugar(new BigDecimal("0"))
                .PolyunsaturatedFat(new BigDecimal("0.5"))
                .MonounsaturatedFat(new BigDecimal("0.5"))
                .build();

        foodRepository.addFood(frogKidneys);

    }
}
