package org.storage.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.storage.StorageRepository;
import org.storage.client.FoodClient;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Storage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final FoodClient foodClient;

    public List<StorageView> getAllByRecordId(Long recordId) {
        return storageRepository.findAllByRecordId(recordId)
                .stream()
                .map(this::toStorageView)
                .collect(Collectors.toList());

    }
    
    public List<StorageView> recordFirstCreation(Long recordID) {
        List<Storage> storageEntities = List.of(new Storage("First Meal", recordID),
                        new Storage("Second Meal", recordID),
                        new Storage("Third Meal", recordID),
                        new Storage("Snacks", recordID));

        storageRepository.saveAllAndFlush(storageEntities);
        return storageEntities.stream()
                .map(this::toStorageView)
                .toList();
    }

    private StorageView toStorageView(Storage entity) {
        StorageView storageView = new StorageView();
        storageView.setId(entity.getId());
        storageView.setRecordId(entity.getRecordId());
        storageView.setName(entity.getName());
        storageView.setFoods(foodClient.getAllFoodsByListNames(entity.getFoodNames()));
        storageView.setRecordId(entity.getRecordId());
        
        return storageView;
    }
}
