package org.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.storage.model.entity.Storage;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StorageControllerIntegrationTest {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllStorages_ValidINput_statusOK() throws Exception {
        Long validRecordId = 1L;
        Storage storage = createStorage(validRecordId);
        storageRepository.save(storage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage?recordId=" + validRecordId)
                .header("X-ViewUser", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk());
      
    }

    private Storage createStorage(Long recordId) {
        Storage entity = new Storage();
        entity.setName("test");
        entity.setRecordId(recordId);
        return entity;
    }

}
