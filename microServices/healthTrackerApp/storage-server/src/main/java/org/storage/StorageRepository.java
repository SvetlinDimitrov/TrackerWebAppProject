package org.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.storage.model.entity.Storage;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    void deleteAllByRecordIdAndUserId(Long recordId, Long userId);

    List<Storage> findAllByRecordIdAndUserId(Long recordId, Long userId);

    Optional<Storage> findByIdAndRecordIdAndUserId(Long id, Long recordId, Long userId);

    Optional<Storage> findByNameAndRecordIdAndUserId(String name, Long recordId, Long userId);
    void deleteAllByRecordId(Long recordId);
}
