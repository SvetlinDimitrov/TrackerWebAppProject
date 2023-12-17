package org.record.services;

import java.util.List;
import java.util.stream.Collectors;

import org.record.RecordRepository;
import org.record.client.StorageClient;
import org.record.client.dto.User;
import org.record.exceptions.InvalidJsonTokenException;
import org.record.exceptions.RecordNotFoundException;
import org.record.model.dtos.RecordView;
import org.record.model.entity.Record;
import org.record.utils.GsonWrapper;
import org.record.utils.NutrientIntakeCreator;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImp extends AbstractRecordService {

    private final StorageClient storageClient;;

    public RecordServiceImp(
            RecordRepository recordRepository,
            NutrientIntakeCreator nutrientIntakeCreator,
            StorageClient storageClient,
            GsonWrapper gsonWrapper) {
        super(gsonWrapper, recordRepository, nutrientIntakeCreator);
        this.storageClient = storageClient;
    }

    public List<RecordView> getAllViewsByUserId(String userToken) throws InvalidJsonTokenException {

        User user = getUserId(userToken);

        return recordRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(record -> toRecordView(record, storageClient.getAllStorages(record.getId(), userToken).getBody(),
                        user))
                .collect(Collectors.toList());
    }

    public RecordView getViewByRecordIdAndUserId(Long recordId, String userToken)
        throws RecordNotFoundException, InvalidJsonTokenException {

        Record record = getRecordByIdAndUserId(recordId, userToken);

        return toRecordView(record, storageClient.getAllStorages(record.getId(), userToken).getBody(),
                getUserId(userToken));
    }

    
}
