package org.nutrition;

import org.nutrition.model.entity.NutritionIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NutritionIntakeRepository extends JpaRepository<NutritionIntake, Long> {
    List<NutritionIntake> findAllByRecordId(Long recordId);

    long deleteAllByRecordId(Long recordId);

    Optional<NutritionIntake> findByRecordIdAndNutrientName(Long recordId , String name);
}
