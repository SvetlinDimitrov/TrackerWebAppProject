package org.trackerwebapp.trackerwebapp.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.header.Header;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.trackerwebapp.trackerwebapp.domain.dto.custom_food.CustomCalorieView;
import org.trackerwebapp.trackerwebapp.domain.dto.custom_food.CustomFoodView;
import org.trackerwebapp.trackerwebapp.domain.dto.custom_food.CustomInsertFoodDto;
import org.trackerwebapp.trackerwebapp.domain.dto.custom_food.CustomNutritionView;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserCreate;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserView;
import org.trackerwebapp.trackerwebapp.domain.enums.AllowedFoodUnits;
import org.trackerwebapp.trackerwebapp.domain.enums.AllowedNutrients;
import org.trackerwebapp.trackerwebapp.enums.Credentials;
import org.trackerwebapp.trackerwebapp.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Testcontainers
class CustomFoodControllerIT {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private UserRepository userRepository;

  @Container
  public static GenericContainer<?> mysqlContainer = new GenericContainer<>("mysql:latest")
      .withExposedPorts(3306)
      .withEnv("MYSQL_ROOT_PASSWORD", "12345")
      .withEnv("MYSQL_DATABASE", "reactiveDB");

  @BeforeAll
  static void beforeAll() {
    mysqlContainer.start();
  }

  @AfterEach
  void afterEach() {
    cleanupDatabase().block();
  }

  private Mono<Void> cleanupDatabase() {
    return userRepository.findAll()
        .flatMap(user -> userRepository.deleteById(user.getId()))
        .then();
  }

  @DynamicPropertySource
  static void setDatasourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.r2dbc.url", () -> "r2dbc:mysql://" + mysqlContainer.getHost() + ":" + mysqlContainer.getFirstMappedPort() + "/reactiveDB");
    registry.add("spring.liquibase.url", () -> "jdbc:mysql://" + mysqlContainer.getHost() + ":" + mysqlContainer.getFirstMappedPort() + "/reactiveDB");
  }

  @Test
  void givenNoAuth_whenTestingGetAllCustomFoods_thenServerMustReturnUnauthorized() {

    webTestClient
        .get()
        .uri("/api/custom/food")
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenAuth_whenTestingGetAllCustomFoods_thenServerMustReturnOkWithEmptyList() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(CustomFoodView.class)
        .value(list -> assertEquals(0, list.size()));
  }

  @Test
  void givenAuth_whenTestingGetAllCustomFoodsWithSpecificValues_thenServerMustReturnOkWithNotEmptyList() {

    int COUNT_CUSTOM_FOOD = 15;
    Header authHeader = setUpUserAndReturnAuthHeader();
    for (int i = 0; i < COUNT_CUSTOM_FOOD; i++) {
      insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions(String.valueOf(i)));
    }

    webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(CustomFoodView.class)
        .value(list -> assertEquals(COUNT_CUSTOM_FOOD, list.size()));
  }

  @Test
  void givenNoAuth_whenTestingGetCustomFoodById_thenServerMustReturnUnauthorized() {

    String RANDOM_FOOD_ID = UUID.randomUUID().toString();

    webTestClient
        .get()
        .uri("/api/custom/food/" + RANDOM_FOOD_ID)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenAuthButInvalidFoodId_whenTestingGetCustomFoodById_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    String INVALID_FOOD_ID = UUID.randomUUID().toString();

    webTestClient
        .get()
        .uri("/api/custom/food/" + INVALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndValidFoodId_whenTestingGetCustomFoodById_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    CustomInsertFoodDto validInsertedFood = createValidInsertedFoodWithEmptyNutritions(String.valueOf(0));
    insertCustomFood(authHeader, validInsertedFood);

    String VALID_FOOD_ID = extractFoodId(authHeader);

    webTestClient
        .get()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(CustomFoodView.class)
        .value(food -> assertEquals(VALID_FOOD_ID, food.id()))
        .value(food -> assertEquals(validInsertedFood.measurement(), food.measurement()))
        .value(food -> assertEquals(validInsertedFood.name(), food.name()))
        .value(food -> assertEquals(0, validInsertedFood.size().compareTo(food.size())))
        .value(food -> assertEquals(0, validInsertedFood.calories().amount().compareTo(food.calories().amount())))
        .value(food -> assertEquals(0, food.nutrients().size()));
  }

  @Test
  void givenAuthAndValidFoodId_whenTestingGetCustomFoodById_thenServerMustReturnOk2() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    CustomInsertFoodDto validInsertedFood = createValidInsertedFoodWithEveryPossibleNutrientView();
    insertCustomFood(authHeader, validInsertedFood);

    String VALID_FOOD_ID = extractFoodId(authHeader);

    Map<String, CustomNutritionView> map = validInsertedFood.nutrients()
        .stream()
        .collect(Collectors.toMap(CustomNutritionView::name, data -> data));


    webTestClient
        .get()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(CustomFoodView.class)
        .value(food -> assertEquals(VALID_FOOD_ID, food.id()))
        .value(food -> {
          food.nutrients()
              .forEach(nutrient -> assertEquals(0, map.get(nutrient.name()).amount().compareTo(nutrient.amount())));
        });
  }

  @Test
  void givenNoAuth_whenTestingAddFood_thenServerMustReturnUnauthorized() {

    webTestClient
        .post()
        .uri("/api/custom/food")
        .bodyValue(createInsertedFood(null, null, null, null, null))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenAuthValidBody_whenTestingAddFood_thenServerMustReturnCreated() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createValidInsertedFoodWithEmptyNutritions("1"))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Void.class);
  }

  @Test
  void givenAuthAndInvalidName_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidFoodNames()
        .forEach(INVALID_NAME -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  INVALID_NAME,
                  createValidCalorieView(),
                  AllowedFoodUnits.GRAM.getSymbol(),
                  BigDecimal.valueOf(350),
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            null,
            createValidCalorieView(),
            AllowedFoodUnits.GRAM.getSymbol(),
            BigDecimal.valueOf(350),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidCaloriesAmount_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidListOfInsertedCalorieAmount()
        .forEach(INVALID_CALORIE -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_FOOD_NAME.getValue(),
                  new CustomCalorieView(INVALID_CALORIE),
                  AllowedFoodUnits.GRAM.getSymbol(),
                  BigDecimal.valueOf(350),
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_FOOD_NAME.getValue(),
            new CustomCalorieView(null),
            AllowedFoodUnits.GRAM.getSymbol(),
            BigDecimal.valueOf(350),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidMeasurement_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidListOfInsertedFoodMeasurements()
        .forEach(INVALID_MEASUREMENT -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_FOOD_NAME.getValue(),
                  createValidCalorieView(),
                  INVALID_MEASUREMENT,
                  new BigDecimal(350),
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_FOOD_NAME.getValue(),
            createValidCalorieView(),
            null,
            new BigDecimal(350),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidAmounts_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidFoodAmounts()
        .forEach(INVALID_FOOD_AMOUNT -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_FOOD_NAME.getValue(),
                  createValidCalorieView(),
                  AllowedFoodUnits.GRAM.getSymbol(),
                  INVALID_FOOD_AMOUNT,
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_FOOD_NAME.getValue(),
            createValidCalorieView(),
            AllowedFoodUnits.GRAM.getSymbol(),
            null,
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidNutrients_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidNutrientViews()
        .forEach(INVALID_NUTRIENT_VIEW -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_FOOD_NAME.getValue(),
                  createValidCalorieView(),
                  AllowedFoodUnits.GRAM.getSymbol(),
                  BigDecimal.valueOf(350),
                  List.of(INVALID_NUTRIENT_VIEW)
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_FOOD_NAME.getValue(),
            createValidCalorieView(),
            AllowedFoodUnits.GRAM.getSymbol(),
            BigDecimal.valueOf(350),
            null
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthValidBodyWithEverySingleNutrientInserted_whenTestingAddFood_thenServerMustReturnCreated() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createValidInsertedFoodWithEveryPossibleNutrientView())
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Void.class);
  }

  @Test
  void givenAuthValidBodyAndDuplicatedNames_whenTestingAddFood_thenServerMustReturnCreatedAndBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createValidInsertedFoodWithEveryPossibleNutrientView())
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Void.class);

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createValidInsertedFoodWithEveryPossibleNutrientView())
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenNoAuth_whenTestingChangeFood_thenServerMustReturnUnauthorized() {

    String INVALID_FOOD_ID = UUID.randomUUID().toString();

    webTestClient
        .put()
        .uri("/api/custom/food/" + INVALID_FOOD_ID)
        .bodyValue(createValidInsertedFoodWithEmptyNutritions("1"))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenAuthInvalidFoodId_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    String INVALID_FOOD_ID = UUID.randomUUID().toString();

    webTestClient
        .put()
        .uri("/api/custom/food/" + INVALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createValidInsertedFoodWithEmptyNutritions("1"))
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(Void.class);
  }

  @Test
  void givenAuthValidFoodIdValidBody_whenTestingChangeFoodWithTheSameNames_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    CustomInsertFoodDto foodDto = createValidInsertedFoodWithEmptyNutritions("1");
    insertCustomFood(authHeader, foodDto);

    String VALID_FOOD_ID = extractFoodId(authHeader);

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(foodDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(CustomFoodView.class);
  }

  @Test
  void givenAuthValidFoodIdValidBody_whenTestingChangeFoodWithTheSameNamesButDifferentId_thenServerMustReturnBadRequest() {

    CustomInsertFoodDto foodDto = createValidInsertedFoodWithEmptyNutritions("1");
    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, foodDto);
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("2"));

    List<CustomFoodView> foodViews = webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(CustomFoodView.class)
        .returnResult()
        .getResponseBody();

    Objects.requireNonNull(foodViews).stream()
        .filter(view -> !view.name().equals(foodDto.name()))
        .forEach(view -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + view.id())
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(foodDto)
              .exchange()
              .expectStatus().isBadRequest();
        });
  }

  @Test
  void givenAuthAndInvalidName_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("1"));

    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidFoodNames()
        .forEach(INVALID_NAME -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  INVALID_NAME,
                  createValidCalorieView(),
                  AllowedFoodUnits.GRAM.getSymbol(),
                  BigDecimal.valueOf(350),
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            null,
            createValidCalorieView(),
            AllowedFoodUnits.GRAM.getSymbol(),
            BigDecimal.valueOf(350),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidCaloriesAmount_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("1"));

    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidListOfInsertedCalorieAmount()
        .forEach(INVALID_CALORIE -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_FOOD_NAME.getValue(),
                  new CustomCalorieView(INVALID_CALORIE),
                  AllowedFoodUnits.GRAM.getSymbol(),
                  BigDecimal.valueOf(350),
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_FOOD_NAME.getValue(),
            new CustomCalorieView(null),
            AllowedFoodUnits.GRAM.getSymbol(),
            BigDecimal.valueOf(350),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidMeasurement_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("1"));

    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidListOfInsertedFoodMeasurements()
        .forEach(INVALID_MEASUREMENT -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_FOOD_NAME.getValue(),
                  createValidCalorieView(),
                  INVALID_MEASUREMENT,
                  new BigDecimal(350),
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_FOOD_NAME.getValue(),
            createValidCalorieView(),
            null,
            new BigDecimal(350),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidAmounts_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("1"));

    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidFoodAmounts()
        .forEach(INVALID_FOOD_AMOUNT -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_FOOD_NAME.getValue(),
                  createValidCalorieView(),
                  AllowedFoodUnits.GRAM.getSymbol(),
                  INVALID_FOOD_AMOUNT,
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_FOOD_NAME.getValue(),
            createValidCalorieView(),
            AllowedFoodUnits.GRAM.getSymbol(),
            null,
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidNutrients_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("1"));

    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidNutrientViews()
        .forEach(INVALID_NUTRIENT_VIEW -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_FOOD_NAME.getValue(),
                  createValidCalorieView(),
                  AllowedFoodUnits.GRAM.getSymbol(),
                  BigDecimal.valueOf(350),
                  List.of(INVALID_NUTRIENT_VIEW)
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_FOOD_NAME.getValue(),
            createValidCalorieView(),
            AllowedFoodUnits.GRAM.getSymbol(),
            BigDecimal.valueOf(350),
            null
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthValidBodyWithEverySingleNutrientInserted_whenTestingChangeFood_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("1"));

    String VALID_FOOD_ID = extractFoodId(authHeader);

    CustomInsertFoodDto insertFoodDto = createValidInsertedFoodWithEveryPossibleNutrientView();

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(insertFoodDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(CustomFoodView.class)
        .value(food -> assertNotNull(food.id()))
        .value(food -> assertEquals(insertFoodDto.name(), food.name()))
        .value(food -> assertEquals(insertFoodDto.measurement(), food.measurement()))
        .value(food -> assertEquals(0, insertFoodDto.size().compareTo(food.size())))
        .value(food -> assertEquals(0, insertFoodDto.calories().amount().compareTo(food.calories().amount())))
        .value(food -> {
          Map<String, CustomNutritionView> map = insertFoodDto.nutrients()
              .stream()
              .collect(Collectors.toMap(CustomNutritionView::name, a -> a));

          assertTrue(
              food.nutrients()
                  .stream()
                  .allMatch(data -> map.get(data.name()).amount().compareTo(data.amount()) == 0)
          );
        });
  }

  @Test
  void givenNoAuth_whenTestingDeleteFood_thenServerMustReturnUnauthorized() {

    String VALID_FOOD_ID = UUID.randomUUID().toString();

    webTestClient
        .delete()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenAuthAndInvalidFoodId_whenTestingDeleteFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    String INVALID_FOOD_ID = UUID.randomUUID().toString();

    webTestClient
        .delete()
        .uri("/api/custom/food/" + INVALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(Void.class);
  }

  @Test
  void givenAuthAndValidFoodId_whenTestingDeleteFood_thenServerMustReturnNoContent() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("1"));

    String VALID_FOOD_ID = extractFoodId(authHeader);

    webTestClient
        .delete()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isNoContent()
        .expectBody(Void.class);
  }

  @Test
  void givenAuthAndValidFoodId_whenTestingDeleteFood2Times_thenServerMustReturnNoContentAndBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions("1"));

    String VALID_FOOD_ID = extractFoodId(authHeader);

    webTestClient
        .delete()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isNoContent()
        .expectBody(Void.class);

    webTestClient
        .delete()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(Void.class);
  }

  @Test
  void givenAuthAndValidFoodId_whenTestingDeleteFood3_thenServerMustReturnNoContentAndBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    int NUMBER_FOOD_TO_INSERT = 5;
    for (int i = 0; i < NUMBER_FOOD_TO_INSERT; i++) {
      insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions(String.valueOf(i)));
    }

    List<String> FOOD_IDS = Objects.requireNonNull(webTestClient
            .get()
            .uri("/api/custom/food")
            .header(authHeader.getName(), authHeader.getValues().getFirst())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(CustomFoodView.class)
            .value(list -> assertEquals(NUMBER_FOOD_TO_INSERT, list.size()))
            .returnResult()
            .getResponseBody())
        .stream()
        .map(CustomFoodView::id)
        .toList();

    for (int i = 0; i < NUMBER_FOOD_TO_INSERT; i++) {
      webTestClient
          .delete()
          .uri("/api/custom/food/" + FOOD_IDS.get(i))
          .header(authHeader.getName(), authHeader.getValues().getFirst())
          .exchange()
          .expectStatus().isNoContent()
          .expectBody(Void.class);
    }
    webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(CustomFoodView.class)
        .value(list -> assertEquals(0, list.size()));
  }

  private String extractFoodId(Header authHeader) {
    return Objects.requireNonNull(webTestClient
            .get()
            .uri("/api/custom/food")
            .header(authHeader.getName(), authHeader.getValues().getFirst())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(CustomFoodView.class)
            .returnResult()
            .getResponseBody())
        .getFirst()
        .id();
  }

  private CustomInsertFoodDto createInsertedFood(String name, CustomCalorieView calorieView, String measurement, BigDecimal size, List<CustomNutritionView> nutritionViewList) {
    return new CustomInsertFoodDto(name, calorieView, measurement, size, nutritionViewList);
  }

  private CustomCalorieView createValidCalorieView() {
    return new CustomCalorieView(BigDecimal.valueOf(366));
  }

  private CustomInsertFoodDto createValidInsertedFoodWithEmptyNutritions(String prefix) {
    return createInsertedFood(
        Credentials.VALID_MEAL_NAME.getValue() + " " + prefix,
        createValidCalorieView(),
        AllowedFoodUnits.GRAM.getSymbol(),
        BigDecimal.valueOf(150),
        List.of()
    );
  }

  private CustomInsertFoodDto createValidInsertedFoodWithEveryPossibleNutrientView() {

    List<CustomNutritionView> nutritionViews = Arrays.stream(AllowedNutrients.values())
        .map(data -> new CustomNutritionView(data.getNutrientName(), data.getNutrientUnit(), BigDecimal.ONE))
        .toList();

    return createInsertedFood(
        Credentials.VALID_MEAL_NAME.getValue(),
        createValidCalorieView(),
        AllowedFoodUnits.GRAM.getSymbol(),
        BigDecimal.valueOf(150),
        nutritionViews
    );
  }

  private Header setUpUserAndReturnAuthHeader() {

    UserCreate newUser = createUser(
        Credentials.VALID_USERNAME.getValue(),
        Credentials.VALID_EMAIL.getValue(),
        Credentials.VALID_PASSWORD.getValue()
    );

    webTestClient.post()
        .uri("/api/user")
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserView.class)
        .returnResult();

    return new Header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()));
  }

  private void insertCustomFood(Header authHeader, CustomInsertFoodDto foodDto) {
    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(foodDto)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Void.class)
        .returnResult();
  }

  private List<String> getInvalidListOfInsertedFoodMeasurements() {

    //MIN 2 chars are required
    return List.of(
        "L",
        "liter                 ",
        "mg",
        "mmg",
        ""
    );
  }

  private List<BigDecimal> getInvalidListOfInsertedCalorieAmount() {
    return List.of(
        BigDecimal.valueOf(0),
        BigDecimal.valueOf(0.99999),
        BigDecimal.valueOf(-1),
        BigDecimal.valueOf(-9999999)
    );
  }

  private List<String> getInvalidFoodNames() {
    return List.of(
        "l",
        "   .   ",
        "",
        "!"
    );
  }

  private List<BigDecimal> getInvalidFoodAmounts() {
    return List.of(
        BigDecimal.valueOf(-1),
        BigDecimal.valueOf(-0.99999999),
        BigDecimal.valueOf(0.99999999),
        BigDecimal.valueOf(0)
    );
  }

  private List<CustomNutritionView> getInvalidNutrientViews() {
    return List.of(
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), null, BigDecimal.ONE),
        new CustomNutritionView(null, AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.ONE),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), null),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(0.9999)),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(0)),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(-1)),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(-99999)),
        new CustomNutritionView("VitaminC", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new CustomNutritionView("", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new CustomNutritionView("CATS", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), "", BigDecimal.valueOf(1)),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), "cats", BigDecimal.valueOf(1)),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), "g", BigDecimal.valueOf(1)),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), "l", BigDecimal.valueOf(1)),
        new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), "ug", BigDecimal.valueOf(1))
    );
  }

  private UserCreate createUser(String username, String email, String password) {
    return new UserCreate(username, email, password);
  }
}