package org.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.storage.model.entity.Storage;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    void deleteAllByRecordId(Long recordId);
    List<Storage> findAllByRecordId(Long recordId);
    Optional<Storage> findByNameAndRecordId(String name , Long recordId);
    Optional<Storage> findByIdAndRecordId(Long id , Long recordId);
    void deleteByIdAndRecordId(Long id , Long recordId);
}
