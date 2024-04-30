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
import org.trackerwebapp.trackerwebapp.domain.dto.meal.CalorieView;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.FoodView;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.InsertFoodDto;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.NutritionView;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserCreate;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserView;
import org.trackerwebapp.trackerwebapp.enums.Credentials;
import org.trackerwebapp.trackerwebapp.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.trackerwebapp.trackerwebapp.utils.FoodUtils.*;

//TODO TEST SERVING AND FOOD INFO IN INSERT_FOOD_DTO
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
    return userRepository.findAllUsers()
        .flatMap(user -> userRepository.deleteUserById(user.getId()))
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
        .expectBodyList(FoodView.class)
        .value(list -> assertEquals(0, list.size()));
  }

  @Test
  void givenAuth_whenTestingGetAllCustomFoodsWithSpecificValues_thenServerMustReturnOkWithNotEmptyList() {

    int COUNT_CUSTOM_FOOD = 15;
    Header authHeader = setUpUserAndReturnAuthHeader();
    for (int i = 0; i < COUNT_CUSTOM_FOOD; i++) {
      insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());
    }

    webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(FoodView.class)
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
    InsertFoodDto validInsertedFood = createValidInsertedFoodWithEmptyNutritions();
    insertCustomFood(authHeader, validInsertedFood);

    String VALID_FOOD_ID = extractFoodId(authHeader);

    webTestClient
        .get()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(FoodView.class)
        .value(food -> assertEquals(VALID_FOOD_ID, food.id()))
//        .value(food -> assertEquals(validInsertedFood.measurement(), food.measurement()))
        .value(food -> assertEquals(validInsertedFood.name(), food.name()))
//        .value(food -> assertEquals(0, validInsertedFood.size().compareTo(food.size())))
        .value(food -> assertEquals(0, validInsertedFood.calories().amount().compareTo(food.calorie().amount())))
        .value(food -> assertEquals(0, food.nutritionList().size()));
  }

  @Test
  void givenAuthAndValidFoodId_whenTestingGetCustomFoodById_thenServerMustReturnOk2() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    InsertFoodDto validInsertedFood = createValidInsertedFoodWithEveryPossibleNutrientView();
    insertCustomFood(authHeader, validInsertedFood);

    String VALID_FOOD_ID = extractFoodId(authHeader);

    Map<String, NutritionView> map = validInsertedFood.nutrients()
        .stream()
        .collect(Collectors.toMap(NutritionView::name, data -> data));


    webTestClient
        .get()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(FoodView.class)
        .value(food -> assertEquals(VALID_FOOD_ID, food.id()))
        .value(food -> {
          food.nutritionList()
              .forEach(nutrient -> assertEquals(0, map.get(nutrient.name()).amount().compareTo(nutrient.amount())));
        });
  }

  @Test
  void givenNoAuth_whenTestingAddFood_thenServerMustReturnUnauthorized() {

    webTestClient
        .post()
        .uri("/api/custom/food")
        .bodyValue(createInsertedFood(null, null, null, null, null, null))
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
        .bodyValue(createValidInsertedFoodWithEmptyNutritions())
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Void.class);
  }

  @Test
  void givenAuthAndInvalidName_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidListOfInsertedFoodNames()
        .forEach(INVALID_NAME -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  INVALID_NAME,
                  createValidCalorieView(),
                  createValidServingView(),
                  createValidFoodInfoView(),
                  List.of(),
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
            createValidServingView(),
            createValidFoodInfoView(),
            List.of(),
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
                  Credentials.VALID_MEAL_NAME.getValue(),
                  new CalorieView(INVALID_CALORIE, Credentials.VALID_FOOD_MEASURE_UNIT.getValue()),
                  createValidServingView(),
                  createValidFoodInfoView(),
                  List.of(),
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
            Credentials.VALID_MEAL_NAME.getValue(),
            new CalorieView(null, Credentials.VALID_FOOD_MEASURE_UNIT.getValue()),
            createValidServingView(),
            createValidFoodInfoView(),
            List.of(),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

//  @Test
//  void givenAuthAndInvalidAmounts_whenTestingAddFood_thenServerMustReturnBadRequest() {
//
//    Header authHeader = setUpUserAndReturnAuthHeader();
//
//    getInvalidFoodAmounts()
//        .forEach(INVALID_FOOD_AMOUNT -> {
//          webTestClient
//              .post()
//              .uri("/api/custom/food")
//              .header(authHeader.getName(), authHeader.getValues().getFirst())
//              .bodyValue(createInsertedFood(
//                  Credentials.VALID_FOOD_NAME.getValue(),
//                  createValidCalorieView(),
//                  Credentials.VALID_FOOD_MEASURE_UNIT.getValue(),
//                  INVALID_FOOD_AMOUNT,
//                  List.of()
//              ))
//              .exchange()
//              .expectStatus().isBadRequest();
//        });
//
//    webTestClient
//        .post()
//        .uri("/api/custom/food")
//        .header(authHeader.getName(), authHeader.getValues().getFirst())
//        .bodyValue(createInsertedFood(
//            Credentials.VALID_FOOD_NAME.getValue(),
//            createValidCalorieView(),
//            Credentials.VALID_FOOD_MEASURE_UNIT.getValue(),
//            null,
//            List.of()
//        ))
//        .exchange()
//        .expectStatus().isBadRequest();
//  }

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
                  Credentials.VALID_MEAL_NAME.getValue(),
                  createValidCalorieView(),
                  createValidServingView(),
                  createValidFoodInfoView(),
                  List.of(),
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
            Credentials.VALID_MEAL_NAME.getValue(),
            createValidCalorieView(),
            createValidServingView(),
            createValidFoodInfoView(),
            List.of(),
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
  void givenAuthValidBodyAndDuplicatedNames_whenTestingAddFood_thenServerMustReturnCreated() {

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
        .expectStatus().isCreated()
        .expectBody(Void.class);
  }

  @Test
  void givenNoAuth_whenTestingChangeFood_thenServerMustReturnUnauthorized() {

    String INVALID_FOOD_ID = UUID.randomUUID().toString();

    webTestClient
        .put()
        .uri("/api/custom/food/" + INVALID_FOOD_ID)
        .bodyValue(createValidInsertedFoodWithEmptyNutritions())
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
        .bodyValue(createValidInsertedFoodWithEmptyNutritions())
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(Void.class);
  }

  @Test
  void givenAuthValidFoodIdValidBody_whenTestingChangeFoodWithTheSameNames_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    InsertFoodDto foodDto = createValidInsertedFoodWithEmptyNutritions();
    insertCustomFood(authHeader, foodDto);

    String VALID_FOOD_ID = extractFoodId(authHeader);

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(foodDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(FoodView.class);
  }

  @Test
  void givenAuthValidFoodIdValidBody_whenTestingChangeFoodWithTheSameNamesButDifferentId_thenServerMustReturnBadRequest() {

    InsertFoodDto foodDto = createValidInsertedFoodWithEmptyNutritions();
    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, foodDto);
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());

    List<FoodView> foodViews = webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(FoodView.class)
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
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());

    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidListOfInsertedFoodNames()
        .forEach(INVALID_NAME -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  INVALID_NAME,
                  createValidCalorieView(),
                  createValidServingView(),
                  createValidFoodInfoView(),
                  List.of(),
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
            createValidServingView(),
            createValidFoodInfoView(),
            List.of(),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidCaloriesAmount_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());

    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidListOfInsertedCalorieAmount()
        .forEach(INVALID_CALORIE -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_MEAL_NAME.getValue(),
                  new CalorieView(INVALID_CALORIE, Credentials.VALID_FOOD_MEASURE_UNIT.getValue()),
                  createValidServingView(),
                  createValidFoodInfoView(),
                  List.of(),
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
            Credentials.VALID_MEAL_NAME.getValue(),
            new CalorieView(null, Credentials.VALID_FOOD_MEASURE_UNIT.getValue()),
            createValidServingView(),
            createValidFoodInfoView(),
            List.of(),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

//  @Test
//  void givenAuthAndInvalidAmounts_whenTestingChangeFood_thenServerMustReturnBadRequest() {
//
//    Header authHeader = setUpUserAndReturnAuthHeader();
//    insertCustomFood(authHeader, FoodUtils.createValidInsertedFoodWithEmptyNutritions());
//
//    String VALID_FOOD_ID = extractFoodId(authHeader);
//
//    getInvalidFoodAmounts()
//        .forEach(INVALID_FOOD_AMOUNT -> {
//          webTestClient
//              .put()
//              .uri("/api/custom/food/" + VALID_FOOD_ID)
//              .header(authHeader.getName(), authHeader.getValues().getFirst())
//              .bodyValue(createInsertedFood(
//                  Credentials.VALID_FOOD_NAME.getValue(),
//                  createValidCalorieView(),
//                  Credentials.VALID_FOOD_MEASURE_UNIT.getValue(),
//                  INVALID_FOOD_AMOUNT,
//                  List.of()
//              ))
//              .exchange()
//              .expectStatus().isBadRequest();
//        });
//
//    webTestClient
//        .put()
//        .uri("/api/custom/food/" + VALID_FOOD_ID)
//        .header(authHeader.getName(), authHeader.getValues().getFirst())
//        .bodyValue(createInsertedFood(
//            Credentials.VALID_FOOD_NAME.getValue(),
//            createValidCalorieView(),
//            Credentials.VALID_FOOD_MEASURE_UNIT.getValue(),
//            null,
//            List.of()
//        ))
//        .exchange()
//        .expectStatus().isBadRequest();
//  }

  @Test
  void givenAuthAndInvalidNutrients_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());

    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidNutrientViews()
        .forEach(INVALID_NUTRIENT_VIEW -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_MEAL_NAME.getValue(),
                  createValidCalorieView(),
                  createValidServingView(),
                  createValidFoodInfoView(),
                  List.of(),
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
            Credentials.VALID_MEAL_NAME.getValue(),
            createValidCalorieView(),
            createValidServingView(),
            createValidFoodInfoView(),
            List.of(),
            null
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthValidBodyWithEverySingleNutrientInserted_whenTestingChangeFood_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());

    String VALID_FOOD_ID = extractFoodId(authHeader);

    InsertFoodDto insertFoodDto = createValidInsertedFoodWithEveryPossibleNutrientView();

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(insertFoodDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(FoodView.class)
        .value(food -> assertNotNull(food.id()))
        .value(food -> assertEquals(insertFoodDto.name(), food.name()))
//        .value(food -> assertEquals(insertFoodDto.measurement(), food.measurement()))
//        .value(food -> assertEquals(0, insertFoodDto.size().compareTo(food.size())))
        .value(food -> assertEquals(0, insertFoodDto.calories().amount().compareTo(food.calorie().amount())))
        .value(food -> {
          Map<String, NutritionView> map = insertFoodDto.nutrients()
              .stream()
              .collect(Collectors.toMap(NutritionView::name, a -> a));
          assertTrue(
              food.nutritionList()
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
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());

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
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());

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
      insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());
    }

    List<String> FOOD_IDS = Objects.requireNonNull(webTestClient
            .get()
            .uri("/api/custom/food")
            .header(authHeader.getName(), authHeader.getValues().getFirst())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(FoodView.class)
            .value(list -> assertEquals(NUMBER_FOOD_TO_INSERT, list.size()))
            .returnResult()
            .getResponseBody())
        .stream()
        .map(FoodView::id)
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
        .expectBodyList(FoodView.class)
        .value(list -> assertEquals(0, list.size()));
  }

  private String extractFoodId(Header authHeader) {
    return Objects.requireNonNull(webTestClient
            .get()
            .uri("/api/custom/food")
            .header(authHeader.getName(), authHeader.getValues().getFirst())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(FoodView.class)
            .returnResult()
            .getResponseBody())
        .getFirst()
        .id();
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

  private void insertCustomFood(Header authHeader, InsertFoodDto foodDto) {
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

  private UserCreate createUser(String username, String email, String password) {
    return new UserCreate(username, email, password);
  }
}