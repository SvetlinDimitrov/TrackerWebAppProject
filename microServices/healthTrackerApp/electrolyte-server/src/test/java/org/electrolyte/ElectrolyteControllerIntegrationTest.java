package org.electrolyte;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
public class ElectrolyteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllElectrolytes_ValidInput_ReturnsAllElectrolytes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/electrolyte"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Sodium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Potassium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Calcium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("Magnesium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("Chloride"));
    }

    @Test
    public void getElectrolyteByName_ValidInput_ReturnsElectrolyteView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/electrolyte/Sodium"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sodium"));
    }

    @Test void getElectrolyteByName_InvalidInput_throwsException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/electrolyte/invalid"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Electrolyte with name 'invalid' does not exist in the data."));
    }
    
}
