package org.storage;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.storage.model.entity.Storage;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends MongoRepository<Storage, String> {
    void deleteAllByRecordIdAndUserId(String recordId, String userId);

    List<Storage> findAllByRecordIdAndUserId(String recordId, String userId);

    Optional<Storage> findByIdAndRecordIdAndUserId(String id, String recordId, String userId);

    void deleteAllByRecordId(String recordId);
}
