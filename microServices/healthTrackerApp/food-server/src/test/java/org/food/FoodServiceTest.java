package org.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.food.domain.dtos.Food;
import org.food.exception.FoodListException;
import org.food.repositories.FoodRepository;
import org.food.services.FoodService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;

    @Test
    public void testGetAllFoods_ShouldReturnAllFoods() {
        Food apple = getApple();
        when(foodRepository.getAllFoods()).thenReturn(List.of(apple));

        List<Food> foods = foodService.getAllFoods();

        assertEquals(apple, foods.get(0));
    }

    @Test
    public void testGetFoodByName_validFoodNameNullSize_ShouldReturnFood() throws FoodListException {
        Food apple = getApple();
        when(foodRepository.getByName("Apple")).thenReturn(java.util.Optional.of(apple));

        Food food = foodService.getFoodByName("Apple", null);

        assertEquals(apple, food);
    }
    @Test
    public void testGetFoodByName_validFoodNameWithHigherSize_ShouldReturnFood() throws FoodListException {
        Food apple = getApple();
        when(foodRepository.getByName("Apple")).thenReturn(java.util.Optional.of(apple));

        Food result = foodService.getFoodByName("Apple", 150.0);

        assertNotEquals(apple, result);
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getSize()), result.getSize().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getCalories()), result.getCalories().doubleValue());

        assertEquals(getIncreaseTransformedValue(1.5 , apple.getA()), result.getA().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getD()), result.getD().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getE()), result.getE().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getK()), result.getK().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getC()), result.getC().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getB1()), result.getB1().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getB2()), result.getB2().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getB3()), result.getB3().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getB5()), result.getB5().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getB6()), result.getB6().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getB7()), result.getB7().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getB9()), result.getB9().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getB12()), result.getB12().doubleValue());

        assertEquals(getIncreaseTransformedValue(1.5 , apple.getCalcium()), result.getCalcium().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getPhosphorus()), result.getPhosphorus().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getMagnesium()), result.getMagnesium().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getSodium()), result.getSodium().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getPotassium()), result.getPotassium().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getChloride()), result.getChloride().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getIron()), result.getIron().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getZinc()), result.getZinc().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getCopper()), result.getCopper().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getManganese()), result.getManganese().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getChromium()), result.getChromium().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getMolybdenum()), result.getMolybdenum().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getIodine()), result.getIodine().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getSelenium()), result.getSelenium().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getFluoride()), result.getFluoride().doubleValue());

        assertEquals(getIncreaseTransformedValue(1.5 , apple.getCarbohydrates()), result.getCarbohydrates().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getProtein()), result.getProtein().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getFat()), result.getFat().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getFiber()), result.getFiber().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getTransFat()), result.getTransFat().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getSaturatedFat()), result.getSaturatedFat().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getSugar()), result.getSugar().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getPolyunsaturatedFat()), result.getPolyunsaturatedFat().doubleValue());
        assertEquals(getIncreaseTransformedValue(1.5 , apple.getMonounsaturatedFat()), result.getMonounsaturatedFat().doubleValue());

    }
    
    @Test
    public void testGetFoodByName_validFoodNameWithLowerSize_ShouldReturnFood() throws FoodListException {
        Food apple = getApple();
        when(foodRepository.getByName("Apple")).thenReturn(java.util.Optional.of(apple));

        Food result = foodService.getFoodByName("Apple", 50.0);

        assertNotEquals(apple, result);
        assertEquals(getDecreesTransformedValue( apple.getSize(),2), result.getSize().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getCalories(),2), result.getCalories().doubleValue());

        assertEquals(getDecreesTransformedValue( apple.getA(),2), result.getA().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getD(),2), result.getD().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getE(),2), result.getE().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getK(),2), result.getK().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getC(),2), result.getC().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getB1(),2), result.getB1().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getB2(),2), result.getB2().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getB3(),2), result.getB3().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getB5(),2), result.getB5().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getB6(),2), result.getB6().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getB7(),2), result.getB7().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getB9(),2), result.getB9().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getB12(),2), result.getB12().doubleValue());

        assertEquals(getDecreesTransformedValue( apple.getCalcium(),2), result.getCalcium().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getPhosphorus(),2), result.getPhosphorus().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getMagnesium(),2), result.getMagnesium().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getSodium(),2), result.getSodium().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getPotassium(),2), result.getPotassium().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getChloride(),2), result.getChloride().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getIron(),2), result.getIron().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getZinc(),2), result.getZinc().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getCopper(),2), result.getCopper().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getManganese(),2), result.getManganese().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getChromium(),2), result.getChromium().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getMolybdenum(),2), result.getMolybdenum().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getIodine(),2), result.getIodine().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getSelenium(),2), result.getSelenium().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getFluoride(),2), result.getFluoride().doubleValue());

        assertEquals(getDecreesTransformedValue( apple.getCarbohydrates(),2), result.getCarbohydrates().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getProtein(),2), result.getProtein().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getFat(),2), result.getFat().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getFiber(),2), result.getFiber().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getTransFat(),2), result.getTransFat().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getSaturatedFat(),2), result.getSaturatedFat().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getSugar(),2), result.getSugar().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getPolyunsaturatedFat(),2), result.getPolyunsaturatedFat().doubleValue());
        assertEquals(getDecreesTransformedValue( apple.getMonounsaturatedFat(),2), result.getMonounsaturatedFat().doubleValue());
    }
    
    @Test
    public void testGetFoodByName_validFoodNameWithSameSize_ShouldReturnFood() throws FoodListException {
        Food apple = getApple();
        when(foodRepository.getByName("Apple")).thenReturn(java.util.Optional.of(apple));

        Food result = foodService.getFoodByName("Apple", 100.0);

        assertEquals(apple, result);
    }
    
    @Test
    public void testGetFoodByName_invalidFoodName_ShouldThrowException() {
        when(foodRepository.getByName("Invalid")).thenReturn(java.util.Optional.empty());

        assertThrows(FoodListException.class,
        () -> foodService.getFoodByName("Invalid", null));
    }

    private Food getApple() {
        return Food.builder()
                .size(new BigDecimal(100))
                .name("Apple")
                .calories(new BigDecimal("52"))
                .A(new BigDecimal("0"))
                .D(new BigDecimal("0"))
                .E(new BigDecimal("0.18"))
                .K(new BigDecimal("1.5"))
                .C(new BigDecimal("1.5"))
                .B1(new BigDecimal("0.017"))
                .B2(new BigDecimal("0.026"))
                .B3(new BigDecimal("0.091"))
                .B5(new BigDecimal("0.043"))
                .B6(new BigDecimal("0.041"))
                .B7(new BigDecimal("0.009"))
                .B9(new BigDecimal("0.003"))
                .B12(new BigDecimal("0"))
                .Calcium(new BigDecimal("6"))
                .Phosphorus(new BigDecimal("11"))
                .Magnesium(new BigDecimal("5"))
                .Sodium(new BigDecimal("1"))
                .Potassium(new BigDecimal("107"))
                .Chloride(new BigDecimal("0"))
                .Iron(new BigDecimal("0.12"))
                .Zinc(new BigDecimal("0.05"))
                .Copper(new BigDecimal("0.05"))
                .Manganese(new BigDecimal("0.035"))
                .Iodine(new BigDecimal("0"))
                .Selenium(new BigDecimal("0"))
                .Fluoride(new BigDecimal("0"))
                .Chromium(new BigDecimal("0"))
                .Molybdenum(new BigDecimal("0"))
                .Carbohydrates(new BigDecimal("14"))
                .Protein(new BigDecimal("0.3"))
                .Fat(new BigDecimal("0.2"))
                .Fiber(new BigDecimal("2.4"))
                .TransFat(new BigDecimal("0"))
                .SaturatedFat(new BigDecimal("0.02"))
                .Sugar(new BigDecimal("10"))
                .PolyunsaturatedFat(new BigDecimal("0.03"))
                .MonounsaturatedFat(new BigDecimal("0.01"))
                .build();
    }
    private double getIncreaseTransformedValue(Double num , BigDecimal bigDecimal) {
        return BigDecimal.valueOf(num).multiply(bigDecimal ).doubleValue();
    }

    private double getDecreesTransformedValue(BigDecimal num , Integer num2) {
        return num.divide(new BigDecimal(num2) , 2, RoundingMode.HALF_UP ).doubleValue();
    }
}
