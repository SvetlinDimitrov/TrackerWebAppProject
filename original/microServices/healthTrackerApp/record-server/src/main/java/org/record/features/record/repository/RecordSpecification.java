package org.record.features.record.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.record.features.record.entity.Record;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class RecordSpecification implements Specification<Record> {

  private final UUID userId;

  @Override
  public Predicate toPredicate(Root<Record> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    return criteriaBuilder.equal(root.get("userId"), this.userId);
  }
}