package org.record.features.record.repository;

import java.util.UUID;
import org.record.features.record.entity.Record;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RecordRepository extends MongoRepository<Record, String> {
  Optional<Record> findByIdAndUserId(String recordId, UUID userId);

  List<Record> findAllByUserId(UUID id);
}
