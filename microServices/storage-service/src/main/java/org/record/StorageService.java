package org.record;

import lombok.RequiredArgsConstructor;
import org.record.model.dto.RecordCreation;
import org.record.model.dto.StorageView;
import org.record.model.entity.Storage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final StorageRepository storageRepository;

    @KafkaListener(topics = "record-creation", groupId = "group_three", containerFactory = "kafkaListenerCreation")
    public void recordFirstCreation (@Payload RecordCreation recordCreationDto){
        storageRepository.saveAllAndFlush(
                List.of(new Storage("First Meal" , recordCreationDto.getRecordId()),
                        new Storage("Second Meal" , recordCreationDto.getRecordId()),
                        new Storage("Third Meal" , recordCreationDto.getRecordId()),
                        new Storage("Snacks" , recordCreationDto.getRecordId())
                )
        );
    }
    @KafkaListener(topics = "record-deletion", groupId = "group_four", containerFactory = "kafkaListenerDeletion")
    @Transactional
    public void recordDeletion(@Payload Long recordId){
        storageRepository.deleteAllByRecordId(recordId);
    }

    public List<StorageView> getAllByRecordId(Long recordId) {
        return storageRepository.findAllByRecordId(recordId)
                .stream()
                .map(this::toStorageView)
                .collect(Collectors.toList());

    }

    private StorageView toStorageView (Storage entity){
        return new StorageView(entity.getId() , entity.getRecordId() , entity.getName());
    }
}
