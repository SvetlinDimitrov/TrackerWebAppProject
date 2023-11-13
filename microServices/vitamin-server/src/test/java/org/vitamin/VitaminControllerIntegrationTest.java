package org.vitamin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
public class VitaminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllVitamins_VitaminsExist_ReturnsStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/vitamin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("D"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("E"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("K"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("C"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].name").value("B1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[6].name").value("B2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[7].name").value("B3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[8].name").value("B5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[9].name").value("B6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[10].name").value("B7"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[11].name").value("B9"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[12].name").value("B12"));

    }

    @Test 
    public void getVitaminByName_ValidName_ReturnStatusOk() throws Exception {
        String valid = "A";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/vitamin/" +valid))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(valid));
    }

    @Test
    public void getVitaminByName_InvalidName_ReturnsBadRequest() throws Exception{
        String invalidName = "invalid";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/vitamin/" + invalidName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vitamin with name " + invalidName + " does not existed in the data."));
    }
}
