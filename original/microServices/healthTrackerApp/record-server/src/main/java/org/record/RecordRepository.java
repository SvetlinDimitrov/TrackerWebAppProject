package org.record;

import org.record.model.entity.Record;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RecordRepository extends MongoRepository<Record, String> {
    Optional<Record> findByIdAndUserId(String recordId, String userId);

    List<Record> findAllByUserId(String userId);

}
