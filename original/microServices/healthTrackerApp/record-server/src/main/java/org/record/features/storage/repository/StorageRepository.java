package org.record.features.storage.repository;

import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.record.features.storage.entity.Storage;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends MongoRepository<Storage, String> {

    List<Storage> findAllByRecordIdAndRecord_UserId(String recordId, UUID userId);

    Optional<Storage> findByIdAndRecordIdAndRecord_UserId(String id, String recordId, UUID userId);

    Optional<Storage> findByIdAndRecord_UserId(String id, UUID userId);
}
