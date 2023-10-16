package org.record;

import org.record.model.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<Record> findByIdAndUserId(Long recordId , Long userId);
}
