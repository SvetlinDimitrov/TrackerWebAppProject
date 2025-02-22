package org.record.features.meal.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.record.features.meal.dto.MealFilter;
import org.record.features.meal.entity.Meal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class MealSpecification implements Specification<Meal> {

  private final MealFilter filter;

  @Override
  public Predicate toPredicate(
      @NonNull Root<Meal> root,
      @NonNull CriteriaQuery<?> query,
      @NonNull CriteriaBuilder criteriaBuilder) {
    Predicate predicate = criteriaBuilder.conjunction();

    if (filter.getRecordId() != null) {
      predicate = criteriaBuilder.and(predicate,
          criteriaBuilder.equal(root.get("record").get("id"), filter.getRecordId()));
    }

    if (filter.getUserId() != null) {
      predicate = criteriaBuilder.and(predicate,
          criteriaBuilder.equal(root.get("record").get("userId"), filter.getUserId()));
    }

    return predicate;
  }
}
