package org.food.features.custom.repository.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.food.features.custom.dto.CustomFilterCriteria;
import org.food.features.custom.entity.CustomFood;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class CustomFoodSpecification implements Specification<CustomFood> {

  private final UUID userId;
  private final CustomFilterCriteria criteria;

  @Override
  public Predicate toPredicate(Root<CustomFood> root, CriteriaQuery<?> query,
      CriteriaBuilder cb) {
    List<Predicate> predicates = new ArrayList<>();

    predicates.add(cb.equal(root.get("userId"), this.userId));

    if (criteria.name() != null && !criteria.name().isEmpty()) {
      predicates.add(
          cb.like(cb.lower(root.get("description")),
              "%" + criteria.name().toLowerCase() + "%")
      );
    }

    return cb.and(predicates.toArray(new Predicate[0]));
  }
}