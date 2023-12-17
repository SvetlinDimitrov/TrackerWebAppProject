package org.storage.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.storage.StorageRepository;
import org.storage.model.entity.Storage;
import org.storage.utils.GsonWrapper;
import org.storage.client.Record;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageKafkaService {

    private final GsonWrapper gson;
    private final StorageRepository storageRepository;

    @KafkaListener(topics = "RECORD_FIRST_CREATION", groupId = "group_record_first_creation", containerFactory = "kafkaListenerRecordFirstCreation")
    public void recordFirstCreation(String recordToken) {
        Record record = getRecord(recordToken);

        Storage storage = new Storage("First Meal", record.getId(), record.getUserId());
        Storage storage2 = new Storage("Second Meal", record.getId(), record.getUserId());
        Storage storage3 = new Storage("Third Meal", record.getId(), record.getUserId());
        Storage storage4 = new Storage("Snacks", record.getId(), record.getUserId());

        storageRepository.save(storage);
        storageRepository.save(storage2);
        storageRepository.save(storage3);
        storageRepository.save(storage4);
    }

    @Transactional
    @KafkaListener(topics = "RECORD_DELETION", groupId = "group_record_deletion", containerFactory = "kafkaListenerRecordDeletion")
    public void recordDeletion(String recordToken) {
        Record record = getRecord(recordToken);

        storageRepository.deleteAllByRecordId(record.getId());

    }

    private Record getRecord(String recordToken) {
        return gson.fromJson(recordToken, Record.class);
    }
}
