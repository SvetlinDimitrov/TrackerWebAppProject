package org.food.repositories;

import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmbeddedFoodRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<NotCompleteFoodView> findAllProjectedByDescriptionAndName(String collectionName) {
        Query query = new Query();
        query.fields().include("description").include("foodClass");
        return mongoTemplate.find(query, NotCompleteFoodView.class, collectionName);
    }

    public <T> Optional<T> findById(String id, Class<T> clazz , String collectionName){
        return Optional.ofNullable(mongoTemplate.findById(id, clazz, collectionName));
    }

    public List<NotCompleteFoodView> findAllProjectedByRegex(String regexWord, String collectionName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("description").regex(regexWord, "i"));
        query.fields().include("description").include("foodClass");
        return mongoTemplate.find(query, NotCompleteFoodView.class, collectionName);
    }

    public List<FilteredFoodView> executeAggregation(String nutrientType , FilterDataInfo info, String collectionName) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind(nutrientType),
                Aggregation.match(Criteria.where(nutrientType + ".name").is(info.getNutrientName())),
                Aggregation.project("_id", "description")
                        .and(nutrientType + ".name").as("nutrient")
                        .and(nutrientType + ".amount").as("amount")
                        .and(nutrientType + ".unit").as("unit"),
                Aggregation.match(Criteria.where("amount").gte(info.getMin()).lte(info.getMax())),
                Aggregation.sort(info.getDesc() ? Sort.Direction.DESC : Sort.Direction.ASC, "amount"),
                Aggregation.limit(info.getLimit())
        );

        AggregationResults<FilteredFoodView> results = mongoTemplate.aggregate(aggregation, collectionName, FilteredFoodView.class);

        return results.getMappedResults();
    }

}
