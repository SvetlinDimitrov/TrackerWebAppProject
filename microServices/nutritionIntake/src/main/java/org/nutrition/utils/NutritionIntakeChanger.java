package org.nutrition.utils;

import java.math.BigDecimal;

import org.nutrition.model.dtos.FoodView;
import org.nutrition.model.entity.NutritionIntake;

public class NutritionIntakeChanger {

    public static void fillNutritionChanges(NutritionIntake entity, FoodView foodView) {
        
        if (entity.getNutrientName().equals("A") && foodView.getA() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getA()));
        } else if (entity.getNutrientName().equals("D") && foodView.getD() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getD()));
        } else if (entity.getNutrientName().equals("E") && foodView.getE() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getE()));
        } else if (entity.getNutrientName().equals("K") && foodView.getK() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getK()));
        } else if (entity.getNutrientName().equals("B1") && foodView.getB1() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB1()));
        } else if (entity.getNutrientName().equals("B2") && foodView.getB2() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB2()));
        } else if (entity.getNutrientName().equals("B3") && foodView.getB3() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB3()));
        } else if (entity.getNutrientName().equals("B5") && foodView.getB5() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB5()));
        } else if (entity.getNutrientName().equals("B6") && foodView.getB6() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB6()));
        } else if (entity.getNutrientName().equals("B7") && foodView.getB7() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB7()));
        } else if (entity.getNutrientName().equals("B9") && foodView.getB9() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB9()));
        } else if (entity.getNutrientName().equals("B12") && foodView.getB12() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB12()));
        } else if (entity.getNutrientName().equals("C") && foodView.getC() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getC()));
        } else if (entity.getNutrientName().equals("Calcium") && foodView.getCalcium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getCalcium()));
        } else if (entity.getNutrientName().equals("Chloride") && foodView.getChloride() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getChloride()));
        } else if (entity.getNutrientName().equals("Magnesium") && foodView.getMagnesium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getMagnesium()));
        } else if (entity.getNutrientName().equals("Potassium") && foodView.getPotassium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getPotassium()));
        } else if (entity.getNutrientName().equals("Sodium") && foodView.getSodium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getSodium()));
        } else if (entity.getNutrientName().equals("Fat") && foodView.getFat() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getFat()));
        } else if (entity.getNutrientName().equals("Carbohydrates") && foodView.getCarbohydrates() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getCarbohydrates()));
        } else if (entity.getNutrientName().equals("Protein") && foodView.getProtein() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getProtein()));
        }
    }

    public static void removeNutritionChange(NutritionIntake entity, FoodView foodView){
        if (entity.getNutrientName().equals("A") && foodView.getA() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getA()));
        } else if (entity.getNutrientName().equals("D") && foodView.getD() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getD()));
        } else if (entity.getNutrientName().equals("E") && foodView.getE() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getE()));
        } else if (entity.getNutrientName().equals("K") && foodView.getK() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getK()));
        } else if (entity.getNutrientName().equals("B1") && foodView.getB1() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getB1()));
        } else if (entity.getNutrientName().equals("B2") && foodView.getB2() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getB2()));
        } else if (entity.getNutrientName().equals("B3") && foodView.getB3() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getB3()));
        } else if (entity.getNutrientName().equals("B5") && foodView.getB5() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getB5()));
        } else if (entity.getNutrientName().equals("B6") && foodView.getB6() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getB6()));
        } else if (entity.getNutrientName().equals("B7") && foodView.getB7() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getB7()));
        } else if (entity.getNutrientName().equals("B9") && foodView.getB9() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getB9()));
        } else if (entity.getNutrientName().equals("B12") && foodView.getB12() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getB12()));
        } else if (entity.getNutrientName().equals("C") && foodView.getC() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getC()));
        } else if (entity.getNutrientName().equals("Calcium") && foodView.getCalcium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getCalcium()));
        } else if (entity.getNutrientName().equals("Chloride") && foodView.getChloride() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getChloride()));
        } else if (entity.getNutrientName().equals("Magnesium") && foodView.getMagnesium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getMagnesium()));
        } else if (entity.getNutrientName().equals("Potassium") && foodView.getPotassium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getPotassium()));
        } else if (entity.getNutrientName().equals("Sodium") && foodView.getSodium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getSodium()));
        } else if (entity.getNutrientName().equals("Fat") && foodView.getFat() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getFat()));
        } else if (entity.getNutrientName().equals("Carbohydrates") && foodView.getCarbohydrates() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getCarbohydrates()));
        } else if (entity.getNutrientName().equals("Protein") && foodView.getProtein() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().subtract(foodView.getProtein()));
        }
        if(entity.getDailyConsumed().compareTo(BigDecimal.ZERO) < 0){
            entity.setDailyConsumed(BigDecimal.ZERO);
        }
    }
}
