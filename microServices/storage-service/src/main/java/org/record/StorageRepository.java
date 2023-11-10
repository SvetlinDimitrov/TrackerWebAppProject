package org.record;

import org.record.model.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    void deleteAllByRecordId(Long recordId);
    List<Storage> findAllByRecordId(Long recordId);

}
