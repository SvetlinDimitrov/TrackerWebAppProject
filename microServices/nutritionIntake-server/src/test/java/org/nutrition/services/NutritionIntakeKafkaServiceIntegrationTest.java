package org.nutrition.services;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.nutrition.NutritionIntakeRepository;
import org.nutrition.model.dtos.FoodView;
import org.nutrition.model.entity.NutritionIntake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class NutritionIntakeKafkaServiceIntegrationTest {

    @Autowired
    private NutritionIntakeRepository repository;

    @Test
    public void deleteNutritionIntakesByRecordId_ValidInvalidInput_DeletesAllWithCurrentRecordId()
            throws InterruptedException {
        Long recordIdToDelete = 1L;

        KafkaTemplate<String, Long> kafkaTemplate = recordDeletionTemplate();
        kafkaTemplate.send("record-deletion", recordIdToDelete);

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            assertTrue(repository.findAllByRecordId(recordIdToDelete).get().isEmpty());
        });
    }

    @Test
    public void changeNutritionIntakeAdding_ValidFoodView_ChangeSuccessful() {
        Long recordId = 2l;

        FoodView foodToSend = getFoodView(recordId);

        repository.save(createNutritionIntake(recordId));
        storageFillingTemplate().send("storage-filling", foodToSend);

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            assertTrue(repository.findAllByRecordId(recordId).get().get(0).getDailyConsumed()
                    .compareTo(new BigDecimal(100)) == 0);
        });

    }

    @Test
    public void changeNutritionIntakeAdding_InvalidFoodView_throwsRecordNotFound(){
        Long recordId = 2l;
        Long invalidRecordId = 21l;

        FoodView foodToSend = getFoodView(invalidRecordId);

        repository.save(createNutritionIntake(recordId));

        storageFillingTemplate().send("storage-filling", foodToSend);
        // TODO: check for exception
    }

    @Test
    public void changeNutritionIntakeRemoving_ValidFoodView_ChangeSuccessful() {
        Long recordId = 3l;

        FoodView foodToSend = getFoodView(recordId);

        repository.save(createNutritionIntake(recordId));
        storageFillingTemplate().send("storage-removing", foodToSend);

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            assertTrue(repository.findAllByRecordId(recordId).get().get(0).getDailyConsumed()
                    .compareTo(new BigDecimal(0)) == 0);
        });

    }

    private KafkaTemplate<String, Long> recordDeletionTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);

        ProducerFactory<String, Long> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }

    private KafkaTemplate<String, FoodView> storageFillingTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, FoodView> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }

    private FoodView getFoodView(Long recordId) {
        FoodView foodView = new FoodView();
        foodView.setRecordId(recordId);
        foodView.setName("test");
        foodView.setSize(BigDecimal.valueOf(100));
        foodView.setMeasurement("g");
        foodView.setCalories(BigDecimal.valueOf(100));
        foodView.setA(BigDecimal.valueOf(100));
        foodView.setD(BigDecimal.valueOf(100));
        foodView.setE(BigDecimal.valueOf(100));
        foodView.setK(BigDecimal.valueOf(100));
        foodView.setC(BigDecimal.valueOf(100));
        foodView.setB1(BigDecimal.valueOf(100));
        foodView.setB2(BigDecimal.valueOf(100));
        foodView.setB3(BigDecimal.valueOf(100));
        foodView.setB5(BigDecimal.valueOf(100));
        foodView.setB6(BigDecimal.valueOf(100));
        foodView.setB7(BigDecimal.valueOf(100));
        foodView.setB9(BigDecimal.valueOf(100));
        foodView.setB12(BigDecimal.valueOf(100));
        foodView.setSodium(BigDecimal.valueOf(100));
        foodView.setPotassium(BigDecimal.valueOf(100));
        foodView.setCalcium(BigDecimal.valueOf(100));
        foodView.setMagnesium(BigDecimal.valueOf(100));
        foodView.setChloride(BigDecimal.valueOf(100));
        foodView.setCarbohydrates(BigDecimal.valueOf(100));
        foodView.setProtein(BigDecimal.valueOf(100));
        foodView.setFat(BigDecimal.valueOf(100));
        return foodView;
    }

    private NutritionIntake createNutritionIntake(Long recordId) {

        NutritionIntake nutritionIntake = new NutritionIntake();
        nutritionIntake.setRecordId(recordId);
        nutritionIntake.setNutrientName("A");
        nutritionIntake.setNutrientType("Vitamin");
        nutritionIntake.setLowerBoundIntake(BigDecimal.valueOf(100));
        nutritionIntake.setUpperBoundIntake(BigDecimal.valueOf(200));

        return nutritionIntake;
    }
}