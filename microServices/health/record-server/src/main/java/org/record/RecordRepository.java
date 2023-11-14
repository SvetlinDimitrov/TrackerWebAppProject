package org.record;

import java.util.List;
import java.util.Optional;

import org.record.model.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<Record> findByIdAndUserId(Long recordId , Long userId);
    Optional<List<Record>> findAllByUserId(Long userId);
}
