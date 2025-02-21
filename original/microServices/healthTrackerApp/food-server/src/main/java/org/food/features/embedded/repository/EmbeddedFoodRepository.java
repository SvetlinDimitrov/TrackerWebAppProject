package org.food.features.embedded.repository;

import static org.example.domain.food.enums.NutrientType.isMacro;
import static org.example.domain.food.enums.NutrientType.isMineral;
import static org.example.domain.food.enums.NutrientType.isVitamin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.food.embedded.dto.BrandedFoodView;
import org.example.domain.food.embedded.dto.FinalFoodView;
import org.example.domain.food.embedded.dto.SurveyFoodView;
import org.example.domain.food.embedded.entity.BrandedFoodEntity;
import org.example.domain.food.embedded.entity.FinalFoodEntity;
import org.example.domain.food.embedded.entity.SurveyFoodEntity;
import org.example.domain.food.embedded.dto.EmbeddedFilterCriteria;
import org.example.domain.food.shared.dto.FoodView;
import org.example.domain.food.shared.entity.FoodBaseEntity;
import org.food.infrastructure.mappers.EmbeddedFoodMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmbeddedFoodRepository {

  private final MongoTemplate mongoTemplate;
  private final EmbeddedFoodMapper embeddedFoodMapper;

  public <T> Optional<T> findById(String id, String foodClass, Class<T> clazz) {
    Query query = new Query(Criteria.where("id")
        .is(id)
        .and("foodClass")
        .is(foodClass));
    return Optional.ofNullable(mongoTemplate.findOne(query, clazz, "foods"));
  }

  public <T extends FoodView> Page<T> findAll(
      String foodClass,
      Pageable pageable,
      EmbeddedFilterCriteria filterCriteria,
      Class<T> type
  ) {
    Criteria combinedCriteria = createCombinedCriteria(foodClass, filterCriteria);

    long totalMatches = mongoTemplate.count(new Query(combinedCriteria), determineEntityType(type),
        "foods");

    Aggregation paginatedAggregation = createPaginatedAggregation(combinedCriteria, pageable);
    List<T> results = mongoTemplate.aggregate(paginatedAggregation, "foods",
            determineEntityType(type))
        .getMappedResults().stream()
        .map(entity -> embeddedFoodMapper.toView((FoodBaseEntity) entity, type))
        .collect(Collectors.toList());

    return new PageImpl<>(results, pageable, totalMatches);
  }

  private Class<?> determineEntityType(Class<?> type) {
    if (type == BrandedFoodView.class) {
      return BrandedFoodEntity.class;
    } else if (type == FinalFoodView.class) {
      return FinalFoodEntity.class;
    } else if (type == SurveyFoodView.class) {
      return SurveyFoodEntity.class;
    } else {
      throw new IllegalArgumentException("Unsupported view type: " + type);
    }
  }

  private Criteria createCombinedCriteria(String foodClass, EmbeddedFilterCriteria filterCriteria) {
    Criteria criteria = Criteria.where("foodClass").is(foodClass);

    if (filterCriteria.nutrientName() != null && !filterCriteria.nutrientName().isEmpty()) {
      String nutrientField;
      if (isVitamin(filterCriteria.nutrientName())) {
        nutrientField = "vitaminNutrients";
      } else if (isMacro(filterCriteria.nutrientName())) {
        nutrientField = "macroNutrients";
      } else if (isMineral(filterCriteria.nutrientName())) {
        nutrientField = "mineralNutrients";
      } else {
        throw new RuntimeException(
            "Unsupported nutrient type: " + filterCriteria.nutrientName());
      }

      Criteria nutrientCriteria = Criteria.where(nutrientField)
          .elemMatch(
              Criteria.where("name").is(filterCriteria.nutrientName())
                  .and("amount")
                  .gte(Optional.ofNullable(filterCriteria.min()).orElse(Double.MIN_VALUE))
                  .lte(Optional.ofNullable(filterCriteria.max()).orElse(Double.MAX_VALUE))
          );

      criteria = criteria.andOperator(nutrientCriteria);
    }

    return criteria;
  }

  private Aggregation createPaginatedAggregation(Criteria criteria, Pageable pageable) {
    MatchOperation matchOperation = Aggregation.match(criteria);
    SkipOperation skipOperation = Aggregation.skip(pageable.getOffset());
    LimitOperation limitOperation = Aggregation.limit(pageable.getPageSize());

    return Aggregation.newAggregation(
        matchOperation,
        skipOperation,
        limitOperation
    );
  }
}