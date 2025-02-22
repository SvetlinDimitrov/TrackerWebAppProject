package org.record.features.record.repository;

import java.util.Optional;
import org.record.features.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<Record, UUID>, JpaSpecificationExecutor<Record> {

  Optional<Record> findByIdAndUserId(UUID id, UUID userId);

  boolean existsByIdAndUserId(UUID id, UUID userId);

  void deleteByIdAndUserId(UUID id, UUID userId);

  void deleteAllByUserId(UUID userId);
}