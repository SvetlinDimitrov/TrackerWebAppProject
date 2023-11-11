package org.nutrition;

import java.util.List;
import java.util.Optional;

import org.nutrition.model.entity.NutritionIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionIntakeRepository extends JpaRepository<NutritionIntake, Long> {
    Optional<List<NutritionIntake>> findAllByRecordId(Long recordId);


    long deleteAllByRecordId(Long recordId);

    Optional<NutritionIntake> findByRecordIdAndNutrientName(Long recordId , String name);
}
