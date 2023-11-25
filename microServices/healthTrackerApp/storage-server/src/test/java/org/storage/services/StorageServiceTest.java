package org.storage.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.storage.StorageRepository;
import org.storage.StorageService;
import org.storage.client.FoodClient;
import org.storage.exception.StorageException;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Storage;

@ExtendWith(MockitoExtension.class)
public class StorageServiceTest {

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private FoodClient foodClient;

    private ArgumentCaptor<Storage> storageCaptor = ArgumentCaptor.forClass(Storage.class);

    @InjectMocks
    private StorageService storageService;

    @Test
    public void testGetAllByRecordId_ValidInput_ReturnsStorageView() throws StorageException {
        Long recordId = 1L;
        Storage storage = createStorage(recordId);
        StorageView storageView = createStorageView(recordId);

        when(storageRepository.findAllByRecordId(recordId)).thenReturn(List.of(storage));

        List<StorageView> result = storageService.getAllByRecordId(recordId);

        assertEquals(result, List.of(storageView));
    }

    @Test
    public void testGetAllByRecordId_InvalidInput_throwsStorageException() throws StorageException {
        Long recordId = 2L;

        when(storageRepository.findAllByRecordId(recordId)).thenReturn(List.of());

        assertThrows(StorageException.class, () -> storageService.getAllByRecordId(recordId));
    }

    @Test
    public void testGetStorageByIdAndRecordId_ValidInput_ReturnsStorageView() throws StorageException {

        Long recordId = 3L;
        Storage storage = createStorage(recordId);
        StorageView storageView = createStorageView(recordId);

        when(storageRepository.findByIdAndRecordId(storage.getId(), recordId)).thenReturn(Optional.ofNullable(storage));

        StorageView result = storageService.getStorageByIdAndRecordId(storage.getId(), recordId);

        assertEquals(result, storageView);
    }

    @Test
    public void testGetStorageByIdAndRecordId_InvalidInput_throwsStorageException() throws StorageException {

        Long recordId = 4L;
        Storage storage = createStorage(recordId);

        when(storageRepository.findByIdAndRecordId(storage.getId(), recordId)).thenReturn(Optional.empty());

        assertThrows(StorageException.class, () -> storageService.getStorageByIdAndRecordId(storage.getId(), recordId));
    }

    @Test
    public void testCreateStorage_NullStorageName_ReturnsDefaultStorageName() throws StorageException {

        Long recordId = 5L;

        Mockito.when(storageRepository.save(storageCaptor.capture())).thenReturn(new Storage());

        storageService.createStorage(recordId, null);

        Storage capturedStorage = storageCaptor.getValue();

        assertTrue(capturedStorage.getName().contains("Default"));
    }

    @Test
    public void testCreateStorage_ProvidedStorageName_StorageNameSavedCorrectly() throws StorageException {
        Long recordId = 6L;

        Mockito.when(storageRepository.save(storageCaptor.capture())).thenReturn(new Storage());

        storageService.createStorage(recordId, "Ivan's Storage");

        Storage capturedStorage = storageCaptor.getValue();

        assertTrue(capturedStorage.getName().equals("Ivan's Storage"));
    }

    @Test
    public void firstCreationStorage_assertFourStorages() throws StorageException {
        Long recordId = 7L;

        Mockito.when(storageRepository.save(storageCaptor.capture())).thenReturn(new Storage());

        storageService.firstCreationStorage(recordId);

        Mockito.verify(storageRepository, Mockito.times(4)).save(storageCaptor.capture());
    }

    @Test
    public void testDeleteStorage_invalidInput_throwsStorageException() {
        Long recordId = 8L;

        Storage storage = createStorage(recordId);

        when(storageRepository
        .findByIdAndRecordId(storage.getId(), recordId))
        .thenReturn(Optional.empty());

        assertThrows(StorageException.class, () -> storageService.deleteStorage(recordId, storage.getId()));
    }
    @Test 
    public void testDeleteStorage_validInput() throws StorageException {
        Long recordId = 9L;

        Storage storage = createStorage(recordId);

        when(storageRepository
        .findByIdAndRecordId(storage.getId(), recordId))
        .thenReturn(Optional.of(storage));

        storageService.deleteStorage(recordId, storage.getId());

        Mockito.verify(storageRepository, Mockito.times(1)).delete(storage);
    }

    @Test
    public void testUpdateStorage_validInput() {
        Long recordId = 10L;

        storageService.deleteAllByRecordId(recordId);

        verify(storageRepository, Mockito.times(1)).deleteAllByRecordId(recordId);
    }
    private Storage createStorage(Long recordId) {
        Storage entity = new Storage();
        entity.setName("test");
        entity.setRecordId(recordId);
        entity.setFoods(new HashMap<>());
        return entity;
    }

    private StorageView createStorageView(Long recordId) {
        StorageView view = new StorageView();
        view.setName("test");
        view.setRecordId(recordId);
        view.setFoods(List.of());
        return view;
    }
}
