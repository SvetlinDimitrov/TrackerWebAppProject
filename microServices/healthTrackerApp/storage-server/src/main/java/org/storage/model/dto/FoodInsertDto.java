package org.storage.model.dto;

import java.math.BigDecimal;

import org.storage.model.entity.Food;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;

@Getter
public class FoodInsertDto {


    public FoodInsertDto() {
        this.size = BigDecimal.ZERO;
        this.calories = BigDecimal.ZERO;
        this.a = BigDecimal.ZERO;
        this.d = BigDecimal.ZERO;
        this.e = BigDecimal.ZERO;
        this.k = BigDecimal.ZERO;
        this.c = BigDecimal.ZERO;
        this.b1 = BigDecimal.ZERO;
        this.b2 = BigDecimal.ZERO;
        this.b3 = BigDecimal.ZERO;
        this.b5 = BigDecimal.ZERO;
        this.b6 = BigDecimal.ZERO;
        this.b7 = BigDecimal.ZERO;
        this.b9 = BigDecimal.ZERO;
        this.b12 = BigDecimal.ZERO;

        this.calcium = BigDecimal.ZERO;
        this.phosphorus = BigDecimal.ZERO;
        this.magnesium = BigDecimal.ZERO;
        this.sodium = BigDecimal.ZERO;
        this.potassium = BigDecimal.ZERO;
        this.chloride = BigDecimal.ZERO;
        this.iron = BigDecimal.ZERO;
        this.zinc = BigDecimal.ZERO;
        this.copper = BigDecimal.ZERO;
        this.manganese = BigDecimal.ZERO;
        this.iodine = BigDecimal.ZERO;
        this.selenium = BigDecimal.ZERO;
        this.fluoride = BigDecimal.ZERO;
        this.chromium = BigDecimal.ZERO;
        this.molybdenum = BigDecimal.ZERO;

        this.carbohydrates = BigDecimal.ZERO;
        this.protein = BigDecimal.ZERO;
        this.fat = BigDecimal.ZERO;
        
        this.sugar = BigDecimal.ZERO;
        this.saturatedFat = BigDecimal.ZERO;
        this.fiber = BigDecimal.ZERO;
        this.transFat = BigDecimal.ZERO;
        this.polyunsaturatedFat = BigDecimal.ZERO;
        this.monounsaturatedFat = BigDecimal.ZERO;
    }

    private Long id;
    private String name;
    private BigDecimal size;
    private BigDecimal calories;
    private BigDecimal a;
    private BigDecimal d;
    private BigDecimal e;
    private BigDecimal k;
    private BigDecimal c;
    private BigDecimal b1;
    private BigDecimal b2;
    private BigDecimal b3;
    private BigDecimal b5;
    private BigDecimal b6;
    private BigDecimal b7;
    private BigDecimal b9;
    private BigDecimal b12;
    private BigDecimal calcium;
    private BigDecimal phosphorus;
    private BigDecimal magnesium;
    private BigDecimal sodium;
    private BigDecimal potassium;
    private BigDecimal chloride;
    private BigDecimal iron;
    private BigDecimal zinc;
    private BigDecimal copper;
    private BigDecimal manganese;
    private BigDecimal iodine;
    private BigDecimal selenium;
    private BigDecimal fluoride;
    private BigDecimal chromium;
    private BigDecimal molybdenum;
    private BigDecimal carbohydrates;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal fiber;
    private BigDecimal transFat;
    private BigDecimal saturatedFat;
    private BigDecimal sugar;
    private BigDecimal polyunsaturatedFat;
    private BigDecimal monounsaturatedFat;

    public void setId(Long id) {
        this.id =  id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSize(BigDecimal size) {
        this.size = (size == null || size.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : size;
    }

    public void setCalories(BigDecimal calories) {
        this.calories = (calories == null || calories.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : calories;
    }

    public void setA(BigDecimal a) {
        this.a = (a == null || a.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : a;
    }

    @JsonSetter
    public void setD(BigDecimal d) {
        this.d = (d == null || d.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : d;
    }

    public void setE(BigDecimal e) {
        this.e = (e == null || e.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : e;
    }

    public void setK(BigDecimal k) {
        this.k = (k == null || k.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : k;
    }

    public void setC(BigDecimal c) {
        this.c = (c == null || c.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : c;
    }

    public void setB1(BigDecimal b1) {
        this.b1 = (b1 == null || b1.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : b1;
    }

    public void setB2(BigDecimal b2) {
        this.b2 = (b2 == null || b2.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : b2;
    }

    public void setB3(BigDecimal b3) {
        this.b3 = (b3 == null || b3.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : b3;
    }

    public void setB5(BigDecimal b5) {
        this.b5 = (b5 == null || b5.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : b5;
    }

    public void setB6(BigDecimal b6) {
        this.b6 = (b6 == null || b6.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : b6;
    }

    public void setB7(BigDecimal b7) {
        this.b7 = (b7 == null || b7.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : b7;
    }

    public void setB9(BigDecimal b9) {
        this.b9 = (b9 == null || b9.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : b9;
    }

    public void setB12(BigDecimal b12) {
        this.b12 = (b12 == null || b12.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : b12;
    }

    public void setCalcium(BigDecimal calcium) {
        this.calcium = (calcium == null || calcium.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : calcium;
    }

    public void setPhosphorus(BigDecimal phosphorus) {
        this.phosphorus = (phosphorus == null || phosphorus.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO
                : phosphorus;
    }

    public void setMagnesium(BigDecimal magnesium) {
        this.magnesium = (magnesium == null || magnesium.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : magnesium;
    }

    public void setSodium(BigDecimal sodium) {
        this.sodium = (sodium == null || sodium.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : sodium;
    }

    public void setPotassium(BigDecimal potassium) {
        this.potassium = (potassium == null || potassium.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : potassium;
    }

    public void setChloride(BigDecimal chloride) {
        this.chloride = (chloride == null || chloride.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : chloride;
    }

    public void setIron(BigDecimal iron) {
        this.iron = (iron == null || iron.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : iron;
    }

    public void setZinc(BigDecimal zinc) {
        this.zinc = (zinc == null || zinc.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : zinc;
    }

    public void setCopper(BigDecimal copper) {
        this.copper = (copper == null || copper.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : copper;
    }

    public void setManganese(BigDecimal manganese) {
        this.manganese = (manganese == null || manganese.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : manganese;
    }

    public void setIodine(BigDecimal iodine) {
        this.iodine = (iodine == null || iodine.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : iodine;
    }

    public void setSelenium(BigDecimal selenium) {
        this.selenium = (selenium == null || selenium.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : selenium;
    }

    public void setFluoride(BigDecimal fluoride) {
        this.fluoride = (fluoride == null || fluoride.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : fluoride;
    }

    public void setChromium(BigDecimal chromium) {
        this.chromium = (chromium == null || chromium.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : chromium;
    }

    public void setMolybdenum(BigDecimal molybdenum) {
        this.molybdenum = (molybdenum == null || molybdenum.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO
                : molybdenum;
    }

    public void setCarbohydrates(BigDecimal carbohydrates) {
        this.carbohydrates = (carbohydrates == null || carbohydrates.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO
                : carbohydrates;
    }

    public void setProtein(BigDecimal protein) {
        this.protein = (protein == null || protein.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : protein;
    }

    public void setFat(BigDecimal fat) {
        this.fat = (fat == null || fat.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : fat;
    }

    public void setFiber(BigDecimal fiber) {
        this.fiber = (fiber == null || fiber.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : fiber;
    }

    public void setSugar(BigDecimal sugar) {
        this.sugar = (sugar == null || sugar.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : sugar;
    }

    public void setTransFat(BigDecimal transFat) {
        this.transFat = (transFat == null || transFat.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : transFat;
    }

    public void setSaturatedFat(BigDecimal saturatedFat) {
        this.saturatedFat = (saturatedFat == null || saturatedFat.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO
                : saturatedFat;
    }

    public void setPolyunsaturatedFat(BigDecimal polyunsaturatedFat) {
        this.polyunsaturatedFat = (polyunsaturatedFat == null || polyunsaturatedFat.compareTo(BigDecimal.ZERO) < 0)
                ? BigDecimal.ZERO
                : polyunsaturatedFat;
    }

    public void setMonounsaturatedFat(BigDecimal monounsaturatedFat) {
        this.monounsaturatedFat = (monounsaturatedFat == null || monounsaturatedFat.compareTo(BigDecimal.ZERO) < 0)
                ? BigDecimal.ZERO
                : monounsaturatedFat;
    }

    public Food toFood() {
        Food food = new Food();
        food.setId(id);
        food.setName(name);
        food.setSize(size);
        food.setCalories(calories);
        food.setMeasurement("grams");
        food.setA(a);
        food.setD(d);
        food.setE(e);
        food.setK(k);
        food.setC(c);
        food.setB1(b1);
        food.setB2(b2);
        food.setB3(b3);
        food.setB5(b5);
        food.setB6(b6);
        food.setB7(b7);
        food.setB9(b9);
        food.setB12(b12);
        food.setCalcium(calcium);
        food.setPhosphorus(phosphorus);
        food.setMagnesium(magnesium);
        food.setSodium(sodium);
        food.setPotassium(potassium);
        food.setChloride(chloride);
        food.setIron(iron);
        food.setZinc(zinc);
        food.setCopper(copper);
        food.setManganese(manganese);
        food.setIodine(iodine);
        food.setSelenium(selenium);
        food.setFluoride(fluoride);
        food.setChromium(chromium);
        food.setMolybdenum(molybdenum);
        food.setCarbohydrates(carbohydrates);
        food.setProtein(protein);
        food.setFat(fat);
        food.setFiber(fiber);
        food.setTransFat(transFat);
        food.setSaturatedFat(saturatedFat);
        food.setSugar(sugar);
        food.setPolyunsaturatedFat(polyunsaturatedFat);
        food.setMonounsaturatedFat(monounsaturatedFat);

        return food;
    }
}
