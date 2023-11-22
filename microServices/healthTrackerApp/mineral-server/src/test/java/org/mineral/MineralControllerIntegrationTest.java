package org.mineral;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
public class MineralControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllMinerals_ValidInput_ReturnsAllMinerals() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mineral"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Calcium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Phosphorus"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Magnesium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("Sodium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("Potassium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].name").value("Chloride"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[6].name").value("Iron"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[7].name").value("Zinc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[8].name").value("Copper"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[9].name").value("Manganese"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[10].name").value("Iodine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[11].name").value("Selenium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[12].name").value("Fluoride"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[13].name").value("Chromium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[14].name").value("Molybdenum"));

                
    }

    @Test
    public void getMineralByName_ValidInput_ReturnsMineralsView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mineral/Sodium"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sodium"));
    }

    @Test void getElectrolyteByName_InvalidInput_throwsException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mineral/invalid"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mineral with name 'invalid' does not exist in the data."));
    }
    
}
