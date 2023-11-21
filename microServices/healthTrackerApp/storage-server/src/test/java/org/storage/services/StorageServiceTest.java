// package org.storage.services;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// import java.util.List;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.storage.StorageRepository;
// import org.storage.StorageService;
// import org.storage.client.FoodClient;
// import org.storage.model.dto.StorageView;
// import org.storage.model.entity.Storage;

// @ExtendWith(MockitoExtension.class)
// public class StorageServiceTest {
    
//     @Mock
//     private StorageRepository storageRepository;

//     @Mock
//     private FoodClient foodClient;

//     @InjectMocks
//     private StorageService storageService;

//     @Test 
//     public void testGetAllByRecordId_ValidInput_ReturnsStorageView() {
//         Long recordId = 1L;
//         Storage storage = createStorage(recordId);
//         StorageView storageView = createStorageView(recordId);

//         when(storageRepository.findAllByRecordId(recordId)).thenReturn(List.of(storage));
//         when(foodClient.getAllFoodsByListNames(storage.getFoodNames())).thenReturn(List.of());
//         List<StorageView> result =storageService.getAllByRecordId(recordId);

//         assertEquals(result, List.of(storageView));
//     }

//     private Storage createStorage(Long recordId) {
//         Storage entity = new Storage();
//         entity.setName("test");
//         entity.setRecordId(recordId);
//         return entity;
//     }

//     private StorageView createStorageView(Long recordId) {
//         StorageView view = new StorageView();
//         view.setName("test");
//         view.setRecordId(recordId);
//         view.setFoods(List.of());
//         return view;
//     }
// }
