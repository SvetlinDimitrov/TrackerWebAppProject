package org.food.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.food.domain.dtos.Food;
import org.food.repositories.FoodRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private FoodRepository foodRepository;

    @Test
    public void testGetAllFoods_ShouldReturnAllFoods() throws Exception {

        Integer expectedSizeOfFoods = foodRepository.getAllFoods().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/food"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Food[] foodArray = gson.fromJson(result.getResponse().getContentAsString(), Food[].class);
        List<Food> foodList = Arrays.asList(foodArray);

        assertEquals(expectedSizeOfFoods, foodList.size());
    }

    @Test
    public void testGetFoodByName_ValidFoodNameNullSize_ShouldReturnCorrectlyTransformFoodWithSize100()
            throws Exception {

        String foodName = "Apple";
        Food food = foodRepository.getByName("Apple").get();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/food/" + foodName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(foodName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(food.getSize().doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calories").value(food.getCalories()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carbohydrates")
                        .value(food.getCarbohydrates()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fat").value(food.getFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.protein").value(food.getProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sugar").value(food.getSugar()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fiber").value(food.getFiber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monounsaturatedFat")
                        .value(food.getMonounsaturatedFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saturatedFat")
                        .value(food.getSaturatedFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transFat").value(food.getTransFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sodium").value(food.getSodium()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calcium").value(food.getCalcium()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chloride").value(food.getChloride()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chromium").value(food.getChromium()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.copper").value(food.getCopper()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fluoride").value(food.getFluoride()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.magnesium").value(food.getMagnesium()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manganese").value(food.getManganese()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.molybdenum").value(food.getMolybdenum()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phosphorus").value(food.getPhosphorus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selenium").value(food.getSelenium()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zinc").value(food.getZinc()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iodine").value(food.getIodine()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iron").value(food.getIron()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.a").value(food.getA()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b1").value(food.getB1()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b2").value(food.getB2()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b3").value(food.getB3()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b5").value(food.getB5()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b6").value(food.getB6()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b7").value(food.getB7()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b9").value(food.getB9()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b12").value(food.getB12()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.c").value(food.getC()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.d").value(food.getD()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.e").value(food.getE()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.k").value(food.getK()));
    }

    @Test
    public void testGetFoodByName_ValidFoodNameValidSizeIncreased_ShouldReturnCorrectlyTransformFood()
            throws Exception {

        String foodName = "Apple";
        String foodName2 = "Almonds";
        Double size = 200.0;
        Food food = foodRepository.getByName("Apple").get();

         mockMvc.perform(MockMvcRequestBuilders.get("/api/food/" + foodName2 + "?amount=" + size))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/food/" + foodName + "?amount=" + size))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(foodName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calories")
                        .value(food.getCalories().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carbohydrates")
                        .value(food.getCarbohydrates().multiply(new BigDecimal(2))
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fat")
                        .value(food.getFat().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.protein")
                        .value(food.getProtein().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sugar")
                        .value(food.getSugar().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fiber")
                        .value(food.getFiber().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monounsaturatedFat")
                        .value(food.getMonounsaturatedFat().multiply(new BigDecimal(2))
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saturatedFat")
                        .value(food.getSaturatedFat().multiply(new BigDecimal(2))
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transFat")
                        .value(food.getTransFat().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sodium")
                        .value(food.getSodium().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calcium")
                        .value(food.getCalcium().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chloride")
                        .value(food.getChloride().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chromium")
                        .value(food.getChromium().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.copper")
                        .value(food.getCopper().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fluoride")
                        .value(food.getFluoride().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.magnesium")
                        .value(food.getMagnesium().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manganese")
                        .value(food.getManganese().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.molybdenum")
                        .value(food.getMolybdenum().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phosphorus")
                        .value(food.getPhosphorus().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selenium")
                        .value(food.getSelenium().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zinc")
                        .value(food.getZinc().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iodine")
                        .value(food.getIodine().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iron")
                        .value(food.getIron().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.a")
                        .value(food.getA().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b1")
                        .value(food.getB1().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b2")
                        .value(food.getB2().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b3")
                        .value(food.getB3().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b5")
                        .value(food.getB5().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b6")
                        .value(food.getB6().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b7")
                        .value(food.getB7().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b9")
                        .value(food.getB9().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b12")
                        .value(food.getB12().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.c")
                        .value(food.getC().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.d")
                        .value(food.getD().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.e")
                        .value(food.getE().multiply(new BigDecimal(2)).doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.k")
                        .value(food.getK().multiply(new BigDecimal(2)).doubleValue()));
    }

    @Test
    public void testGetFoodByName_ValidFoodNameValidSizeDecreased_ShouldReturnCorrectlyTransformFood()
            throws Exception {

        String foodName = "Apple";
        Double size = 50.0;
        Food food = foodRepository.getByName("Apple").get();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/food/" + foodName + "?amount=" + size))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(foodName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(size))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calories")
                        .value(food.getCalories()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carbohydrates").value(
                        food.getCarbohydrates()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fat")
                        .value(food.getFat().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.protein")
                        .value(food.getProtein()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sugar")
                        .value(food.getSugar()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fiber")
                        .value(food.getFiber()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monounsaturatedFat").value(
                        food.getMonounsaturatedFat()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saturatedFat")
                        .value(food.getSaturatedFat()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transFat")
                        .value(food.getTransFat()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sodium")
                        .value(food.getSodium()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calcium")
                        .value(food.getCalcium()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chloride")
                        .value(food.getChloride()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chromium")
                        .value(food.getChromium()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.copper")
                        .value(food.getCopper()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fluoride")
                        .value(food.getFluoride()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.magnesium")
                        .value(food.getMagnesium()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manganese")
                        .value(food.getManganese()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.molybdenum")
                        .value(food.getMolybdenum()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phosphorus")
                        .value(food.getPhosphorus()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selenium")
                        .value(food.getSelenium()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zinc")
                        .value(food.getZinc().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iodine")
                        .value(food.getIodine()
                                .divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iron")
                        .value(food.getIron().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.a")
                        .value(food.getA().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b1")
                        .value(food.getB1().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b2")
                        .value(food.getB2().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b3")
                        .value(food.getB3().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b5")
                        .value(food.getB5().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b6")
                        .value(food.getB6().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b7")
                        .value(food.getB7().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b9")
                        .value(food.getB9().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b12")
                        .value(food.getB12().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.c")
                        .value(food.getC().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.d")
                        .value(food.getD().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.e")
                        .value(food.getE().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.k")
                        .value(food.getK().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)
                                .doubleValue()));
    }

    @Test
    public void testGetFoodByName_InvalidFoodName_ShouldReturnErrorResponse() throws Exception {

        String foodName = "InvalidFoodName";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/food/" + foodName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
