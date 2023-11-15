package org.nutrition;

import org.junit.jupiter.api.Test;
import org.nutrition.model.entity.NutritionIntake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class NutritionIntakeControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NutritionIntakeRepository repository;

    @Test
    public void getAllNutritionByRecord_ValidRecordId_isOK() throws Exception {
        
        NutritionIntake nutrient = new NutritionIntake();
        nutrient.setRecordId(10l);

        repository.save(nutrient);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/nutritionIntake")
                .param("recordId", "10")
                .header("X-ViewUser", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    } 
    
    @Test
    public void getAllNutritionByRecord_InvalidRecordId_isBadRequest() throws Exception {
    
        mockMvc.perform(MockMvcRequestBuilders.get("/api/nutritionIntake")
                .param("recordId", "11")
                .header("X-ViewUser", "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    } 

    
}
