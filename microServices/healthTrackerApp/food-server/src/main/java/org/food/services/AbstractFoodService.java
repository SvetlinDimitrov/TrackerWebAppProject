package org.food.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.food.domain.dtos.Food;

public abstract class AbstractFoodService {

    protected Food calculateFoodByAmount(Food food, Double size) {

        BigDecimal amount = BigDecimal.valueOf(size);

        if (food.getSize().compareTo(amount) == 0) {
            return food;
        } else if (food.getSize().compareTo(amount) > 0) {

            BigDecimal multiplayer = food.getSize().divide(amount, 2, RoundingMode.HALF_UP);

            Food baseFood = new Food();
            baseFood.setName(food.getName());
            baseFood.setSize(amount);
            baseFood.setCalories(food.getCalories().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setA(food.getA().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setD(food.getD().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setE(food.getE().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setK(food.getK().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setC(food.getC().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB1(food.getB1().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB2(food.getB2().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB3(food.getB3().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB5(food.getB5().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB6(food.getB6().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB7(food.getB7().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB9(food.getB9().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB12(food.getB12().divide(multiplayer, 2, RoundingMode.HALF_UP));

            baseFood.setCalcium(food.getCalcium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setPhosphorus(food.getPhosphorus().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setMagnesium(food.getMagnesium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setSodium(food.getSodium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setPotassium(food.getPotassium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setChloride(food.getChloride().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setIron(food.getIron().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setZinc(food.getZinc().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setCopper(food.getCopper().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setManganese(food.getManganese().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setIodine(food.getIodine().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setSelenium(food.getSelenium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setFluoride(food.getFluoride().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setChromium(food.getChromium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setMolybdenum(food.getMolybdenum().divide(multiplayer, 2, RoundingMode.HALF_UP));

            baseFood.setCarbohydrates(food.getCarbohydrates().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setProtein(food.getProtein().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setFat(food.getFat().divide(multiplayer, 2, RoundingMode.HALF_UP));

            baseFood.setFiber(food.getFiber().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setTransFat(food.getTransFat().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setSaturatedFat(food.getSaturatedFat().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setSugar(food.getSugar().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setPolyunsaturatedFat(food.getPolyunsaturatedFat().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setMonounsaturatedFat(food.getMonounsaturatedFat().divide(multiplayer, 2, RoundingMode.HALF_UP));

            return baseFood;
        } else {
            BigDecimal multiplayer = amount.divide(food.getSize(), 2, RoundingMode.HALF_UP);
            Food baseFood = new Food();
            baseFood.setName(food.getName());
            baseFood.setSize(amount);
            baseFood.setCalories(food.getCalories().multiply(multiplayer));
            baseFood.setA(food.getA().multiply(multiplayer));
            baseFood.setD(food.getD().multiply(multiplayer));
            baseFood.setE(food.getE().multiply(multiplayer));
            baseFood.setK(food.getK().multiply(multiplayer));
            baseFood.setC(food.getC().multiply(multiplayer));
            baseFood.setB1(food.getB1().multiply(multiplayer));
            baseFood.setB2(food.getB2().multiply(multiplayer));
            baseFood.setB3(food.getB3().multiply(multiplayer));
            baseFood.setB5(food.getB5().multiply(multiplayer));
            baseFood.setB6(food.getB6().multiply(multiplayer));
            baseFood.setB7(food.getB7().multiply(multiplayer));
            baseFood.setB9(food.getB9().multiply(multiplayer));
            baseFood.setB12(food.getB12().multiply(multiplayer));

            baseFood.setCalcium(food.getCalcium().multiply(multiplayer));
            baseFood.setPhosphorus(food.getPhosphorus().multiply(multiplayer));
            baseFood.setMagnesium(food.getMagnesium().multiply(multiplayer));
            baseFood.setSodium(food.getSodium().multiply(multiplayer));
            baseFood.setPotassium(food.getPotassium().multiply(multiplayer));
            baseFood.setChloride(food.getChloride().multiply(multiplayer));
            baseFood.setIron(food.getIron().multiply(multiplayer));
            baseFood.setZinc(food.getZinc().multiply(multiplayer));
            baseFood.setCopper(food.getCopper().multiply(multiplayer));
            baseFood.setManganese(food.getManganese().multiply(multiplayer));
            baseFood.setIodine(food.getIodine().multiply(multiplayer));
            baseFood.setSelenium(food.getSelenium().multiply(multiplayer));
            baseFood.setFluoride(food.getFluoride().multiply(multiplayer));
            baseFood.setChromium(food.getChromium().multiply(multiplayer));
            baseFood.setMolybdenum(food.getMolybdenum().multiply(multiplayer));

            baseFood.setCarbohydrates(food.getCarbohydrates().multiply(multiplayer));
            baseFood.setProtein(food.getProtein().multiply(multiplayer));
            baseFood.setFat(food.getFat().multiply(multiplayer));

            baseFood.setFiber(food.getFiber().multiply(multiplayer));
            baseFood.setTransFat(food.getTransFat().multiply(multiplayer));
            baseFood.setSaturatedFat(food.getSaturatedFat().multiply(multiplayer));
            baseFood.setSugar(food.getSugar().multiply(multiplayer));
            baseFood.setPolyunsaturatedFat(food.getPolyunsaturatedFat().multiply(multiplayer));
            baseFood.setMonounsaturatedFat(food.getMonounsaturatedFat().multiply(multiplayer));

            return baseFood;
        }
    }

}
