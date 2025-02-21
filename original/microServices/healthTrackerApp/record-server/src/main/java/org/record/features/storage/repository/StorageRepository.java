package org.record.features.storage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.record.features.storage.entity.Storage;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends MongoRepository<Storage, String> {

    List<Storage> findAllByRecordIdAndUserId(String recordId, String userId);

    Optional<Storage> findByIdAndRecordIdAndUserId(String id, String recordId, String userId);

    Optional<Storage> findByIdAndUserId(String id, String userId);

    void deleteAllByRecordId(String recordId);
}
