package org.storage.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.storage.StorageRepository;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Storage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final StorageRepository storageRepository;

    public List<StorageView> getAllByRecordId(Long recordId) {
        return storageRepository.findAllByRecordId(recordId)
                .stream()
                .map(this::toStorageView)
                .collect(Collectors.toList());

    }

    private StorageView toStorageView(Storage entity) {
        return new StorageView(entity.getId(), entity.getRecordId(), entity.getName(), entity.getFoodNames());
    }
}
