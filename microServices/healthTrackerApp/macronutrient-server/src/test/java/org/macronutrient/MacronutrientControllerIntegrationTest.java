package org.macronutrient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class MacronutrientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllMacros_shouldReturnAllMacros() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/macronutrient"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Carbohydrates"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Protein"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Fat"));
    }

    @Test
    public void getAllMacrosTypes_shouldReturnAllMacrosTypes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/macronutrient/types"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Sugar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Fiber"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Saturated Fat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("Polyunsaturated Fat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("Monounsaturated Fat"));

    }

    @Test
    public void getMacronutrientByName_ValidInput_statusOk() throws Exception {
        String validName = "Carbohydrates";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/macronutrient/" + validName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Carbohydrates"));

    }

    @Test
    public void getMacronutrientByName_InvalidInput_statusBadRequest() throws Exception {
        String invalidName = "InvalidInput";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/macronutrient/" + invalidName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Macronutrient with name '" + invalidName + "' does not existed in the data."));

    }

    @Test
    public void getMacronutrientTypeByName_ValidInput_statusOk() throws Exception {
        String validName = "Sugar";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/macronutrient/types/" + validName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sugar"));

    }

    @Test
    public void getMacronutrientTypeByName_InvalidInput_statusBadRequest() throws Exception {
        
        String invalidName = "InvalidInput";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/macronutrient/types/" + invalidName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Macronutrient with name '" + invalidName + "' does not existed in the data."));

    }
}
