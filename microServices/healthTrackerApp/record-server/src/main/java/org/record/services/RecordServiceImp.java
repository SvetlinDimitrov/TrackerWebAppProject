package org.record.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.record.RecordRepository;
import org.record.client.StorageClient;
import org.record.client.dto.User;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.StorageException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.RecordView;
import org.record.model.entity.Record;
import org.record.utils.NutrientIntakeCreator;
import org.record.utils.RecordUtils;
import org.record.utils.RecordValidator;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import feign.FeignException.FeignClientException;

@Service
public class RecordServiceImp extends AbstractRecordService {

    private final StorageClient storageClient;;

    public RecordServiceImp(
            Gson gson,
            RecordRepository recordRepository,
            NutrientIntakeCreator nutrientIntakeCreator,
            StorageClient storageClient) {
        super(gson, recordRepository, nutrientIntakeCreator);
        this.storageClient = storageClient;
    }

    public List<RecordView> getAllViewsByUserId(String userToken) throws UserNotFoundException {

        List<Record> records = recordRepository
                .findAllByUserId(getUserId(userToken).getId())
                .orElseThrow(() -> new UserNotFoundException(
                        getUserId(userToken).getUsername() + " does not have any records"));

        User user = getUserId(userToken);

        return records
                .stream()
                .map(record -> toRecordView(record, storageClient.getAllStorages(record.getId(),userToken).getBody(), user))
                .collect(Collectors.toList());
    }

    public RecordView getViewByRecordIdAndUserId(Long recordId, String userToken) throws RecordNotFoundException {

        Record record = getRecordByIdAndUserId(recordId, userToken);

        return toRecordView(record, storageClient.getAllStorages(record.getId() , userToken).getBody(), getUserId(userToken));
    }

    public void addNewRecordByUserId(String userToken, String name) throws RecordCreationException {
        User user = getUserId(userToken);

        RecordValidator.validateRecord(user);

        Record record = new Record();

        record.setDate(LocalDate.now());

        if (name == null || name.isBlank()) {
            record.setName("Default" + RecordUtils.generateRandomNumbers(4));
        } else {
            record.setName(name);
        }

        BigDecimal BMR = RecordUtils.getBmr(user);
        BigDecimal caloriesPerDay = RecordUtils.getCaloriesPerDay(user, BMR);

        record.setDailyCalories(caloriesPerDay);
        record.setUserId(user.getId());

        recordRepository.saveAndFlush(record);

        storageClient.createStorageFirstCreation(record.getId(), userToken);
    }

    public void deleteById(Long recordId, String userToken) throws RecordNotFoundException, StorageException {
        Record record = getRecordByIdAndUserId(recordId, userToken);

        try{
            storageClient.deleteAllStoragesByRecordId(record.getId() , userToken);
        } catch (FeignClientException e) {
            throw new StorageException("Storages with record id " + record.getId() + " not found");
        }

        recordRepository.deleteById(record.getId());
    }

    public void createNewStorage(Long recordId, String storageName, String userToken) throws RecordNotFoundException {
        Record record = getRecordByIdAndUserId(recordId, userToken);
        
        storageClient.createStorage(storageName, record.getId(), userToken);
    }

    public void removeStorage(Long recordId, Long storageId, String userToken) throws RecordNotFoundException, StorageException {
        Record record = getRecordByIdAndUserId(recordId, userToken);

        try{
            storageClient.deleteStorage(storageId, record.getId(), userToken);
        } catch (Exception e) {
            throw new StorageException("Storage with id " + storageId + " not found");
        }
        
    }
}
