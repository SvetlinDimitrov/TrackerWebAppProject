package org.nutriGuideBuddy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nutriGuideBuddy.domain.dto.meal.CalorieView;
import org.nutriGuideBuddy.domain.dto.meal.FoodView;
import org.nutriGuideBuddy.domain.dto.meal.InsertFoodDto;
import org.nutriGuideBuddy.domain.dto.meal.NutritionView;
import org.nutriGuideBuddy.domain.dto.user.JwtResponse;
import org.nutriGuideBuddy.domain.dto.user.UserCreate;
import org.nutriGuideBuddy.domain.enums.AllowedCalorieUnits;
import org.nutriGuideBuddy.enums.Credentials;
import org.nutriGuideBuddy.repository.UserRepository;
import org.nutriGuideBuddy.utils.JWTUtilEmailValidation;
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
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.nutriGuideBuddy.utils.FoodUtils.*;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("secret")
@Testcontainers
class CustomFoodControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JWTUtilEmailValidation jwtUtil;

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
  void givenAuth_whenTestingGetAllCustomFoods_thenServerMustReturnOkWithEmptyList() throws JsonProcessingException {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .consumeWith(response -> {
          String responseBody = response.getResponseBody();
          try {
            JsonNode root = new ObjectMapper().readTree(responseBody);
            JsonNode size = root.path("totalElements");

            assertEquals(0, size.asInt());
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });
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
        .expectBody(String.class)
        .consumeWith(response -> {
          String responseBody = response.getResponseBody();
          System.out.println(responseBody);
          try {
            JsonNode root = new ObjectMapper().readTree(responseBody);
            JsonNode size = root.path("totalElements");

            assertEquals(COUNT_CUSTOM_FOOD, size.asInt());
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });
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
                  new CalorieView(INVALID_CALORIE, AllowedCalorieUnits.CALORIE.getSymbol()),
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

  @Test
  void givenAuthAndInvalidServing_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidServings()
        .forEach(INVALID_SERVING -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_MEAL_NAME.getValue(),
                  createValidCalorieView(),
                  INVALID_SERVING,
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
            createValidCalorieView(),
            null,
            createValidFoodInfoView(),
            List.of(),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidListServings_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidServings()
        .forEach(INVALID_SERVING -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_MEAL_NAME.getValue(),
                  createValidCalorieView(),
                  createValidServingView(),
                  createValidFoodInfoView(),
                  List.of(INVALID_SERVING),
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
            createValidCalorieView(),
            createValidServingView(),
            createValidFoodInfoView(),
            null,
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidFoodInfo_whenTestingAddFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    getInvalidFoodInfoViews()
        .forEach(INVALID_FOOD_INFO -> {
          webTestClient
              .post()
              .uri("/api/custom/food")
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_MEAL_NAME.getValue(),
                  createValidCalorieView(),
                  createValidServingView(),
                  INVALID_FOOD_INFO,
                  List.of(),
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });
  }

  @Test
  void givenAuthAndNullFoodInfo_whenTestingAddFood_thenServerMustReturnCreated() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_MEAL_NAME.getValue(),
            createValidCalorieView(),
            createValidServingView(),
            null,
            List.of(),
            List.of()
        ))
        .exchange()
        .expectStatus().isCreated();
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

  @Test
  void givenAuthAndInvalidServing_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());
    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidServings()
        .forEach(INVALID_SERVING -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_MEAL_NAME.getValue(),
                  createValidCalorieView(),
                  INVALID_SERVING,
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
            createValidCalorieView(),
            null,
            createValidFoodInfoView(),
            List.of(),
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidListServings_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());
    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidServings()
        .forEach(INVALID_SERVING -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_MEAL_NAME.getValue(),
                  createValidCalorieView(),
                  createValidServingView(),
                  createValidFoodInfoView(),
                  List.of(INVALID_SERVING),
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
            createValidCalorieView(),
            createValidServingView(),
            createValidFoodInfoView(),
            null,
            List.of()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenAuthAndInvalidFoodInfo_whenTestingChangeFood_thenServerMustReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());
    String VALID_FOOD_ID = extractFoodId(authHeader);

    getInvalidFoodInfoViews()
        .forEach(INVALID_FOOD_INFO -> {
          webTestClient
              .put()
              .uri("/api/custom/food/" + VALID_FOOD_ID)
              .header(authHeader.getName(), authHeader.getValues().getFirst())
              .bodyValue(createInsertedFood(
                  Credentials.VALID_MEAL_NAME.getValue(),
                  createValidCalorieView(),
                  createValidServingView(),
                  INVALID_FOOD_INFO,
                  List.of(),
                  List.of()
              ))
              .exchange()
              .expectStatus().isBadRequest();
        });
  }

  @Test
  void givenAuthAndNullFoodInfo_whenTestingChangeFood_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();
    insertCustomFood(authHeader, createValidInsertedFoodWithEmptyNutritions());
    String VALID_FOOD_ID = extractFoodId(authHeader);

    webTestClient
        .put()
        .uri("/api/custom/food/" + VALID_FOOD_ID)
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createInsertedFood(
            Credentials.VALID_MEAL_NAME.getValue(),
            createValidCalorieView(),
            createValidServingView(),
            null,
            List.of(),
            List.of()
        ))
        .exchange()
        .expectStatus().isOk();
  }

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

    List<String> FOOD_IDS = new ArrayList<>(); // Declare an array to hold the result
    webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .consumeWith(response -> {
          String responseBody = response.getResponseBody();
          try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode content = root.path("content");
            if (content.isArray() && !content.isEmpty()) {
              content.forEach(data -> {
                FOOD_IDS.add(data.path("id").asText());
              });
            }
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });

    FOOD_IDS.forEach(foodId -> {
      webTestClient
          .delete()
          .uri("/api/custom/food/" + foodId)
          .header(authHeader.getName(), authHeader.getValues().getFirst())
          .exchange()
          .expectStatus().isNoContent()
          .expectBody(Void.class);
    });

    webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .consumeWith(response -> {
          String responseBody = response.getResponseBody();
          try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode size = root.path("totalElements");
            assertEquals(0, size.asInt());
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });
  }

  private String extractFoodId(Header authHeader) {
    final String[] extractedId = new String[1]; // Declare an array to hold the result

    webTestClient
        .get()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .consumeWith(response -> {
          String responseBody = response.getResponseBody();
          try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode content = root.path("content");
            if (content.isArray() && !content.isEmpty()) {
              JsonNode firstObject = content.get(0);
              extractedId[0] = firstObject.path("id").asText();
            }
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });

    return extractedId[0];
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

  private Header setUpUserAndReturnAuthHeader() {
    String token = jwtUtil.generateToken(Credentials.VALID_EMAIL.getValue());

    UserCreate newUser = createUser(
        Credentials.VALID_USERNAME.getValue(),
        Credentials.VALID_EMAIL.getValue(),
        Credentials.VALID_PASSWORD.getValue(),
        token
    );

    JwtResponse responseBody = webTestClient.post()
        .uri("/api/user")
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(JwtResponse.class)
        .returnResult()
        .getResponseBody();

    assert responseBody != null;
    return new Header("Authorization", "Bearer " + responseBody.accessToken().value());
  }

  private UserCreate createUser(String username, String email, String password, String token) {
    return new UserCreate(username, email, password, token);
  }
}