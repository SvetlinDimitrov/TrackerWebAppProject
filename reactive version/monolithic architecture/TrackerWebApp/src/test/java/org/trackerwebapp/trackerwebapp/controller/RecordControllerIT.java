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
import org.trackerwebapp.trackerwebapp.domain.dto.NutritionIntakeView;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.*;
import org.trackerwebapp.trackerwebapp.domain.dto.record.CreateRecord;
import org.trackerwebapp.trackerwebapp.domain.dto.record.CustomNutritionView;
import org.trackerwebapp.trackerwebapp.domain.dto.record.DistributedMacros;
import org.trackerwebapp.trackerwebapp.domain.dto.record.RecordView;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserCreate;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserDetailsDto;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserDetailsView;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserView;
import org.trackerwebapp.trackerwebapp.domain.enums.*;
import org.trackerwebapp.trackerwebapp.enums.Credentials;
import org.trackerwebapp.trackerwebapp.repository.UserRepository;
import org.trackerwebapp.trackerwebapp.utils.record.MacronutrientCreator;
import org.trackerwebapp.trackerwebapp.utils.record.MineralCreator;
import org.trackerwebapp.trackerwebapp.utils.record.VitaminCreator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Testcontainers
class RecordControllerIT {

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
  void givenNoAuth_whenTestingViewRecord_thenServerShouldReturnUnauthorized() {

    webTestClient
        .post()
        .uri("/api/record")
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenValidAuthButNotCompletedUserDetails_whenTestingViewRecord_thenServerShouldReturnForbidden() {
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

    UserDetailsView userDetailsView = webTestClient.get()
        .uri("/api/user/details")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .returnResult().getResponseBody();

    webTestClient.post()
        .uri("/api/record")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .exchange()
        .expectStatus().isForbidden();
  }

  @Test
  void givenValidAuthButNotCompletedUserDetailsUntilTheyAreCompleted_whenTestingViewRecord_thenServerShouldReturnOk() {
    UserCreate newUser = createUser(
        Credentials.VALID_USERNAME.getValue(),
        Credentials.VALID_EMAIL.getValue(),
        Credentials.VALID_PASSWORD.getValue()
    );

    UserDetailsDto validDetailsForKilograms = createDetails(
        new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
        null,
        null,
        null,
        null
    );

    webTestClient.post()
        .uri("/api/user")
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserView.class)
        .returnResult();

    webTestClient.patch()
        .uri("/api/user/details")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .bodyValue(validDetailsForKilograms)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .value(detailsView -> assertNull(detailsView.height()))
        .value(detailsView -> assertNull(detailsView.gender()))
        .value(detailsView -> assertNull(detailsView.age()))
        .value(detailsView -> assertNull(detailsView.workoutState()))
        .value(detailsView -> assertEquals(0, validDetailsForKilograms.kilograms().compareTo(detailsView.kilograms())))
        .returnResult();

    webTestClient.post()
        .uri("/api/record")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .returnResult();

    UserDetailsDto validDetailsForAge = createDetails(
        null,
        null,
        24,
        null,
        null
    );

    webTestClient.patch()
        .uri("/api/user/details")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .bodyValue(validDetailsForAge)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .value(detailsView -> assertNull(detailsView.height()))
        .value(detailsView -> assertNull(detailsView.gender()))
        .value(detailsView -> assertNull(detailsView.workoutState()))
        .value(detailsView -> assertEquals(0, validDetailsForKilograms.kilograms().compareTo(detailsView.kilograms())))
        .value(detailsView -> assertEquals(validDetailsForAge.age(), detailsView.age()))
        .returnResult();

    webTestClient.post()
        .uri("/api/record")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .returnResult();

    UserDetailsDto validDetailsForGender = createDetails(
        null,
        null,
        null,
        null,
        Gender.MALE
    );

    webTestClient.patch()
        .uri("/api/user/details")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .bodyValue(validDetailsForGender)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .value(detailsView -> assertNull(detailsView.height()))
        .value(detailsView -> assertNull(detailsView.workoutState()))
        .value(detailsView -> assertEquals(0, validDetailsForKilograms.kilograms().compareTo(detailsView.kilograms())))
        .value(detailsView -> assertEquals(validDetailsForAge.age(), detailsView.age()))
        .value(detailsView -> assertEquals(validDetailsForGender.gender(), detailsView.gender()))
        .returnResult();

    webTestClient.post()
        .uri("/api/record")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .returnResult();

    UserDetailsDto validDetailsForWorkOutState = createDetails(
        null,
        null,
        null,
        WorkoutState.LIGHTLY_ACTIVE,
        null
    );

    webTestClient.patch()
        .uri("/api/user/details")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .bodyValue(validDetailsForWorkOutState)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .value(detailsView -> assertNull(detailsView.height()))
        .value(detailsView -> assertEquals(0, validDetailsForKilograms.kilograms().compareTo(detailsView.kilograms())))
        .value(detailsView -> assertEquals(validDetailsForAge.age(), detailsView.age()))
        .value(detailsView -> assertEquals(validDetailsForGender.gender(), detailsView.gender()))
        .value(detailsView -> assertEquals(validDetailsForWorkOutState.workoutState(), detailsView.workoutState()))
        .returnResult();

    webTestClient.post()
        .uri("/api/record")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .returnResult();

    UserDetailsDto validDetailsForHeight = createDetails(
        null,
        BigDecimal.valueOf(180),
        null,
        null,
        null
    );

    webTestClient.patch()
        .uri("/api/user/details")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .bodyValue(validDetailsForHeight)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .value(detailsView -> assertEquals(0, validDetailsForKilograms.kilograms().compareTo(detailsView.kilograms())))
        .value(detailsView -> assertEquals(0, validDetailsForHeight.height().compareTo(detailsView.height())))
        .value(detailsView -> assertEquals(validDetailsForAge.age(), detailsView.age()))
        .value(detailsView -> assertEquals(validDetailsForGender.gender(), detailsView.gender()))
        .value(detailsView -> assertEquals(validDetailsForWorkOutState.workoutState(), detailsView.workoutState()))
        .returnResult();

    webTestClient.post()
        .uri("/api/record")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .bodyValue(createCreateRecord())
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(record -> assertNotNull(record.getDailyCaloriesToConsume()))
        .value(record -> assertNotNull(record.getDailyCaloriesConsumed()))
        .value(record -> assertNotNull(record.getMacroIntakes()))
        .value(record -> assertNotNull(record.getVitaminIntake()))
        .value(record -> assertNotNull(record.getMineralIntakes()))
        .returnResult();

  }

  @Test
  void givenValidAuthAndFullUserDetailsWithGoal_whenTestingViewRecord_thenServerShouldReturnTheCorrectViewAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();

    RecordView recordWithoutTheGoal = webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord())
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .returnResult()
        .getResponseBody();

    //The consumed calorie should be equal if the goal is to maintain weight
    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(new CreateRecord(Goals.MaintainWeight, null, null))
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(record -> assertEquals(0, record.getDailyCaloriesToConsume()
            .compareTo(Objects.requireNonNull(recordWithoutTheGoal).getDailyCaloriesToConsume()))
        );

    //The consumed calorie should be minus 500 if the goal is to lose weight
    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(new CreateRecord(Goals.LoseWeight, null, null))
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(record -> assertEquals(0, record.getDailyCaloriesToConsume()
            .compareTo(Objects.requireNonNull(recordWithoutTheGoal).getDailyCaloriesToConsume().subtract(BigDecimal.valueOf(500))))
        );

    //The consumed calorie should be plus 500 if the goal is to gain weight
    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(new CreateRecord(Goals.GainWeight, null, null))
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(record -> assertEquals(0, record.getDailyCaloriesToConsume()
            .compareTo(Objects.requireNonNull(recordWithoutTheGoal).getDailyCaloriesToConsume().add(BigDecimal.valueOf(500))))
        );

  }

  @Test
  void givenValidAuthAndFullUserDetailsWithInvalidDistributedMacros_whenTestingViewRecord_thenServerShouldReturnBadRequest() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    DistributedMacros INVALID_DISTRIBUTED_MACROS_1 = new DistributedMacros(BigDecimal.valueOf(0.23), null, null, null, null);
    DistributedMacros INVALID_DISTRIBUTED_MACROS_2 = new DistributedMacros(BigDecimal.valueOf(1), BigDecimal.valueOf(0.99), BigDecimal.valueOf(0.25), BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.1));

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(new CreateRecord(null, INVALID_DISTRIBUTED_MACROS_1, null))
        .exchange()
        .expectStatus().isBadRequest();

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(new CreateRecord(null, INVALID_DISTRIBUTED_MACROS_2, null))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenValidAuthAndFullUserDetails_whenTestingViewRecord_thenServerShouldReturnCorrectVitaminNutrientViewsAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    //Calculating based on https://www.ncbi.nlm.nih.gov/books/NBK56068/table/summarytables.t2/?report=objectonly
    Map<String, NutritionIntakeView> vitaminNutritions = calculateVitaminNutritions();

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord())
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView -> {
          List<NutritionIntakeView> recordViewList = recordView.getVitaminIntake().stream()
              .sorted(Comparator.comparing(NutritionIntakeView::getName))
              .toList();
          List<NutritionIntakeView> vitaminNutritionsList = vitaminNutritions.values().stream()
              .sorted(Comparator.comparing(NutritionIntakeView::getName))
              .toList();

          assertEquals(recordViewList, vitaminNutritionsList);
        })
        .value(recordView -> assertEquals(recordView.getVitaminIntake().size(), VitaminCreator.allAllowedVitamins.size()));
  }

  @Test
  void givenValidAuthAndFullUserDetails_whenTestingViewRecord_thenServerShouldReturnCorrectMineralsNutrientViewsAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    //Calculating based on https://www.ncbi.nlm.nih.gov/books/NBK545442/table/appJ_tab3/?report=objectonly
    Map<String, NutritionIntakeView> mineralsNutrients = calculateMineralsNutritions();

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord())
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView -> {
          List<NutritionIntakeView> recordViewList = recordView.getMineralIntakes().stream()
              .sorted(Comparator.comparing(NutritionIntakeView::getName))
              .toList();
          List<NutritionIntakeView> mineralNutrientsViews = mineralsNutrients.values().stream()
              .sorted(Comparator.comparing(NutritionIntakeView::getName))
              .toList();

          assertIterableEquals(recordViewList, mineralNutrientsViews);
        })
        .value(recordView -> assertEquals(recordView.getMineralIntakes().size(), MineralCreator.allAllowedMinerals.size()));
  }

  @Test
  void givenValidAuthAndFullUserDetails_whenTestingViewRecord_thenServerShouldReturnCorrectMacronutrientsViewsAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord())
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView -> assertEquals(recordView.getMacroIntakes().size(), MacronutrientCreator.allAllowedMacros.size()));
  }

  @Test
  void givenValidAuthAndFullUserDetails_whenTestingViewRecord_thenServerShouldReturnCorrectAllowedNutrientsAmountAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord())
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView ->
            assertEquals(Arrays.stream(AllowedNutrients.values()).toList().size(),
                recordView.getMacroIntakes().size() + recordView.getVitaminIntake().size() + recordView.getMineralIntakes().size())

        );
  }

  @Test
  void givenValidAuthAndFullUserDetailsAndSingleValidCustomNutrientValue_whenTestingViewRecord_thenServerShouldReturnCorrectNutrientsViewsAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    CustomNutritionView customNutritionView = new CustomNutritionView(AllowedNutrients.VitaminE.getNutrientName(), BigDecimal.valueOf(120));

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord(null, null, List.of(customNutritionView)))
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView ->
            assertEquals(
                recordView.getVitaminIntake()
                    .stream()
                    .filter(nutrient -> nutrient.getName().equals(customNutritionView.name()))
                    .findAny()
                    .orElseThrow()
                    .getRecommendedIntake(),
                customNutritionView.recommendedIntake()
            )
        );
  }

  @Test
  void givenValidAuthAndFullUserDetailsAndInvalidCustomNutrientValue_whenTestingViewRecord_thenServerShouldReturnBadRequest() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    CustomNutritionView customNutritionView = new CustomNutritionView("Invalid nutrientName", BigDecimal.valueOf(120));
    CustomNutritionView customNutritionView2 = new CustomNutritionView(AllowedNutrients.VitaminC.getNutrientName(), BigDecimal.valueOf(-120));
    List<CustomNutritionView> INVALID_CUSTOM_NUTRIENTS = List.of(customNutritionView2, customNutritionView);

    for (CustomNutritionView invalidCustomNutrient : INVALID_CUSTOM_NUTRIENTS) {
      webTestClient.post()
          .uri("/api/record")
          .header(authHeader.getName(), authHeader.getValues().getFirst())
          .bodyValue(createCreateRecord(null, null, List.of(invalidCustomNutrient)))
          .exchange()
          .expectStatus().isBadRequest();
    }
  }

  @Test
  void givenValidAuthAndFullUserDetailsAndSingleFood_whenTestingViewRecord_thenServerShouldReturnCorrectRecordViewAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    String VALID_MEAL_ID = obtainValidMealId(authHeader);
    InsertFoodDto validInsertedFoodWithEveryPossibleNutrient = createValidInsertedFoodWithEveryPossibleNutrientView2();
    setUpInsertedFood(authHeader, VALID_MEAL_ID, validInsertedFoodWithEveryPossibleNutrient);
    Map<String, NutritionView> nutritionViewMap = validInsertedFoodWithEveryPossibleNutrient.nutrients()
        .stream()
        .collect(Collectors.toMap(NutritionView::name, data -> data));

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord(null, null, null))
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView -> assertEquals(0, validInsertedFoodWithEveryPossibleNutrient.calories().amount().compareTo(recordView.getDailyCaloriesConsumed())))
        .value(recordView -> {
          recordView.getMineralIntakes()
              .forEach(mineral -> assertEquals(0, mineral.getDailyConsumed().compareTo(nutritionViewMap.get(mineral.getName()).amount())));
        })
        .value(recordView -> {
          recordView.getVitaminIntake()
              .forEach(vitamin -> assertEquals(0, vitamin.getDailyConsumed().compareTo(nutritionViewMap.get(vitamin.getName()).amount())));
        })
        .value(recordView -> {
          recordView.getMacroIntakes()
              .forEach(macro -> assertEquals(0, macro.getDailyConsumed().compareTo(nutritionViewMap.get(macro.getName()).amount())));
        });
  }

  @Test
  void givenValidAuthAndFullUserDetailsAndMultipleFood_whenTestingViewRecord_thenServerShouldReturnCorrectRecordViewAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    String VALID_MEAL_ID = obtainValidMealId(authHeader);
    int FOOD_COUNTER = 20;
    InsertFoodDto food = createValidInsertedFoodWithEveryPossibleNutrientView2();
    for (int i = 0; i < FOOD_COUNTER; i++) {
      setUpInsertedFood(authHeader, VALID_MEAL_ID, food);
    }
    Map<String, NutritionView> nutritionViewMap = food.nutrients()
        .stream()
        .collect(Collectors.toMap(NutritionView::name, data -> data));

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord(null, null, null))
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView -> assertEquals(0, food.calories().amount().multiply(BigDecimal.valueOf(FOOD_COUNTER)).compareTo(recordView.getDailyCaloriesConsumed())))
        .value(recordView -> {
          recordView.getMineralIntakes()
              .forEach(mineral -> assertEquals(0, mineral.getDailyConsumed().compareTo(nutritionViewMap.get(mineral.getName()).amount().multiply(BigDecimal.valueOf(FOOD_COUNTER)))));
        })
        .value(recordView -> {
          recordView.getVitaminIntake()
              .forEach(vitamin -> assertEquals(0, vitamin.getDailyConsumed().compareTo(nutritionViewMap.get(vitamin.getName()).amount().multiply(BigDecimal.valueOf(FOOD_COUNTER)))));
        })
        .value(recordView -> {
          recordView.getMacroIntakes()
              .forEach(macro -> assertEquals(0, macro.getDailyConsumed().compareTo(nutritionViewMap.get(macro.getName()).amount().multiply(BigDecimal.valueOf(FOOD_COUNTER)))));
        });
  }

  @Test
  void givenValidAuthAndFullUserDetailsAndSingleFoodInMultipleMeals_whenTestingViewRecord_thenServerShouldReturnCorrectRecordViewAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    int NUMBER_OF_MEALS_CRATED = 3;
    InsertFoodDto validInsertedFoodWithEveryPossibleNutrient = createValidInsertedFoodWithEveryPossibleNutrientView2();

    for (int i = 0; i < NUMBER_OF_MEALS_CRATED; i++) {
      String VALID_MEAL_ID = obtainValidMealId(authHeader);
      setUpInsertedFood(authHeader, VALID_MEAL_ID, validInsertedFoodWithEveryPossibleNutrient);
    }

    Map<String, NutritionView> nutritionViewMap = validInsertedFoodWithEveryPossibleNutrient.nutrients()
        .stream()
        .collect(Collectors.toMap(NutritionView::name, data -> data));

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord(null, null, null))
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView -> assertEquals(0, validInsertedFoodWithEveryPossibleNutrient.calories().amount().multiply(BigDecimal.valueOf(NUMBER_OF_MEALS_CRATED))
            .compareTo(recordView.getDailyCaloriesConsumed()))
        )
        .value(recordView -> {
          recordView.getMineralIntakes()
              .forEach(mineral -> assertEquals(0,
                  mineral.getDailyConsumed().compareTo(nutritionViewMap.get(mineral.getName()).amount().multiply(BigDecimal.valueOf(NUMBER_OF_MEALS_CRATED))))
              );
        })
        .value(recordView -> {
          recordView.getVitaminIntake()
              .forEach(vitamin -> assertEquals(0,
                  vitamin.getDailyConsumed().compareTo(nutritionViewMap.get(vitamin.getName()).amount().multiply(BigDecimal.valueOf(NUMBER_OF_MEALS_CRATED))))
              );
        })
        .value(recordView -> {
          recordView.getMacroIntakes()
              .forEach(macro -> assertEquals(0,
                  macro.getDailyConsumed().compareTo(nutritionViewMap.get(macro.getName()).amount().multiply(BigDecimal.valueOf(NUMBER_OF_MEALS_CRATED))))
              );
        });
  }

  @Test
  void givenValidAuthAndFullUserDetailsAndMultipleFoodsInMultipleMeals_whenTestingViewRecord_thenServerShouldReturnCorrectRecordViewAndStatusOk() {

    Header authHeader = setUpUserWithFullUserDetailsAndReturnAuthHeader();
    int NUMBER_OF_MEALS_CRATED = 3;
    int NUMBER_OF_FOOD_COUNTER = 10;
    int FINAL_MULTIPLAYER = NUMBER_OF_FOOD_COUNTER * NUMBER_OF_MEALS_CRATED;
    InsertFoodDto validInsertedFoodWithEveryPossibleNutrient = createValidInsertedFoodWithEveryPossibleNutrientView2();

    for (int i = 0; i < NUMBER_OF_MEALS_CRATED; i++) {
      String VALID_MEAL_ID = obtainValidMealId(authHeader);
      for (int j = 0; j < NUMBER_OF_FOOD_COUNTER; j++) {
        setUpInsertedFood(authHeader, VALID_MEAL_ID, validInsertedFoodWithEveryPossibleNutrient);
      }
    }

    Map<String, NutritionView> nutritionViewMap = validInsertedFoodWithEveryPossibleNutrient.nutrients()
        .stream()
        .collect(Collectors.toMap(NutritionView::name, data -> data));

    webTestClient.post()
        .uri("/api/record")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(createCreateRecord(null, null, null))
        .exchange()
        .expectStatus().isOk()
        .expectBody(RecordView.class)
        .value(recordView -> assertEquals(0, validInsertedFoodWithEveryPossibleNutrient.calories().amount().multiply(BigDecimal.valueOf(FINAL_MULTIPLAYER))
            .compareTo(recordView.getDailyCaloriesConsumed()))
        )
        .value(recordView -> {
          recordView.getMineralIntakes()
              .forEach(mineral -> assertEquals(0,
                  mineral.getDailyConsumed().compareTo(nutritionViewMap.get(mineral.getName()).amount().multiply(BigDecimal.valueOf(FINAL_MULTIPLAYER))))
              );
        })
        .value(recordView -> {
          recordView.getVitaminIntake()
              .forEach(vitamin -> assertEquals(0,
                  vitamin.getDailyConsumed().compareTo(nutritionViewMap.get(vitamin.getName()).amount().multiply(BigDecimal.valueOf(FINAL_MULTIPLAYER))))
              );
        })
        .value(recordView -> {
          recordView.getMacroIntakes()
              .forEach(macro -> assertEquals(0,
                  macro.getDailyConsumed().compareTo(nutritionViewMap.get(macro.getName()).amount().multiply(BigDecimal.valueOf(FINAL_MULTIPLAYER))))
              );
        });
  }

  private UserCreate createUser(String username, String email, String password) {
    return new UserCreate(username, email, password);
  }

  private UserDetailsDto createDetails(BigDecimal kilograms, BigDecimal height, Integer age, WorkoutState workoutState, Gender gender) {
    return new UserDetailsDto(kilograms, height, age, workoutState, gender);
  }

  private Map<String, NutritionIntakeView> calculateVitaminNutritions() {
    Map<String, NutritionIntakeView> nutritionIntakeViews = new HashMap<>();
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminA.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminA.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(900))
            .measurement(AllowedNutrients.VitaminA.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminC.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminC.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(90))
            .measurement(AllowedNutrients.VitaminC.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminD_D2_D3.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminD_D2_D3.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(15))
            .measurement(AllowedNutrients.VitaminD_D2_D3.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminE.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminE.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(15))
            .measurement(AllowedNutrients.VitaminE.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminK.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminK.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(120))
            .measurement(AllowedNutrients.VitaminK.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminB1_Thiamin.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminB1_Thiamin.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(1.2))
            .measurement(AllowedNutrients.VitaminB1_Thiamin.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminB2_Riboflavin.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminB2_Riboflavin.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(1.3))
            .measurement(AllowedNutrients.VitaminB2_Riboflavin.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminB3_Niacin.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminB3_Niacin.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(16))
            .measurement(AllowedNutrients.VitaminB3_Niacin.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminB6.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminB6.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(1.3))
            .measurement(AllowedNutrients.VitaminB6.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminB9_Folate.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminB9_Folate.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(400))
            .measurement(AllowedNutrients.VitaminB9_Folate.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminB12.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminB12.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(2.4))
            .measurement(AllowedNutrients.VitaminB12.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminB5_PantothenicAcid.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminB5_PantothenicAcid.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(5))
            .measurement(AllowedNutrients.VitaminB5_PantothenicAcid.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.VitaminB7_Biotin.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.VitaminB7_Biotin.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(30))
            .measurement(AllowedNutrients.VitaminB7_Biotin.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Choline.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Choline.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(550))
            .measurement(AllowedNutrients.Choline.getNutrientUnit())
            .build()
    );
    return nutritionIntakeViews;
  }

  private Map<String, NutritionIntakeView> calculateMineralsNutritions() {

    Map<String, NutritionIntakeView> nutritionIntakeViews = new HashMap<>();

    nutritionIntakeViews.put(
        AllowedNutrients.Calcium_Ca.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Calcium_Ca.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(1000))
            .measurement(AllowedNutrients.Calcium_Ca.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Chromium_Cr.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Chromium_Cr.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(35))
            .measurement(AllowedNutrients.Chromium_Cr.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Copper_Cu.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Copper_Cu.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(900))
            .measurement(AllowedNutrients.Copper_Cu.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Fluoride.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Fluoride.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(4))
            .measurement(AllowedNutrients.Fluoride.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Iodine_I.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Iodine_I.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(150))
            .measurement(AllowedNutrients.Iodine_I.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Iron_Fe.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Iron_Fe.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(8))
            .measurement(AllowedNutrients.Iron_Fe.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Magnesium_Mg.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Magnesium_Mg.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(400))
            .measurement(AllowedNutrients.Magnesium_Mg.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Manganese_Mn.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Manganese_Mn.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(2.3))
            .measurement(AllowedNutrients.Manganese_Mn.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Molybdenum_Mo.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Molybdenum_Mo.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(45))
            .measurement(AllowedNutrients.Molybdenum_Mo.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Phosphorus_P.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Phosphorus_P.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(700))
            .measurement(AllowedNutrients.Phosphorus_P.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Selenium_Se.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Selenium_Se.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(55))
            .measurement(AllowedNutrients.Selenium_Se.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Zinc_Zn.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Zinc_Zn.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(11))
            .measurement(AllowedNutrients.Zinc_Zn.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Potassium_K.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Potassium_K.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(3400))
            .measurement(AllowedNutrients.Potassium_K.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Sodium_Na.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Sodium_Na.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(1500))
            .measurement(AllowedNutrients.Sodium_Na.getNutrientUnit())
            .build()
    );
    nutritionIntakeViews.put(
        AllowedNutrients.Chloride.getNutrientName(),
        NutritionIntakeView.builder()
            .name(AllowedNutrients.Chloride.getNutrientName())
            .dailyConsumed(BigDecimal.ZERO)
            .recommendedIntake(BigDecimal.valueOf(2.3))
            .measurement(AllowedNutrients.Chloride.getNutrientUnit())
            .build()
    );

    return nutritionIntakeViews;
  }

  private List<CustomNutritionView> createCustomNutritionViews(BigDecimal toConsume) {
    return Arrays.stream(AllowedNutrients.values())
        .map(value -> new CustomNutritionView(value.getNutrientName(), toConsume))
        .toList();
  }

  private CreateRecord createCreateRecord() {
    return new CreateRecord(null, null, null);
  }

  private CreateRecord createCreateRecord(Goals goal, DistributedMacros distributedMacros, List<CustomNutritionView> nutritionViews) {
    return new CreateRecord(goal, distributedMacros, nutritionViews);
  }

  private CalorieView createValidCalorieView() {
    return new CalorieView(BigDecimal.valueOf(366), AllowedCalorieUnits.CALORIE.getSymbol());
  }

  private InsertFoodDto createInsertedFood(String name, CalorieView calorieView, String measurement, BigDecimal size, List<NutritionView> nutritionViewList) {
    return new InsertFoodDto(name, calorieView, measurement, size, nutritionViewList);
  }

  private InsertFoodDto createValidInsertedFoodWithEveryPossibleNutrientView2() {

    List<NutritionView> nutritionViews = Arrays.stream(AllowedNutrients.values())
        .map(data -> new NutritionView(data.getNutrientName(), data.getNutrientUnit(), BigDecimal.valueOf(150)))
        .toList();

    return createInsertedFood(
        Credentials.VALID_MEAL_NAME.getValue(),
        createValidCalorieView(),
        AllowedFoodUnits.GRAM.getSymbol(),
        BigDecimal.valueOf(450),
        nutritionViews
    );
  }

  private Header setUpUserWithFullUserDetailsAndReturnAuthHeader() {

    UserCreate newUser = createUser(
        Credentials.VALID_USERNAME.getValue(),
        Credentials.VALID_EMAIL.getValue(),
        Credentials.VALID_PASSWORD.getValue()
    );

    UserDetailsDto validDetails = createDetails(
        new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
        new BigDecimal(Credentials.VALID_DETAIL_HEIGHT.getValue()),
        Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
        WorkoutState.valueOf(Credentials.VALID_DETAIL_WORKOUT.getValue()),
        Gender.valueOf(Credentials.VALID_DETAIL_GENDER.getValue())
    );

    webTestClient.post()
        .uri("/api/user")
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserView.class)
        .returnResult();

    webTestClient.patch()
        .uri("/api/user/details")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()))
        .bodyValue(validDetails)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .returnResult();

    return new Header("Authorization", "Basic " + Base64.getEncoder().encodeToString((newUser.email() + ":" + newUser.password()).getBytes()));
  }

  private void setUpInsertedFood(Header authHeader, String VALID_MEAL_ID, InsertFoodDto foodToInsert) {

    webTestClient
        .post()
        .uri("/api/meals/" + VALID_MEAL_ID + "/insertFood")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(foodToInsert)
        .exchange()
        .expectStatus().isOk();
  }

  private String obtainValidMealId(Header authHeader) {
    return Objects.requireNonNull(
        webTestClient
            .post()
            .uri("/api/meals")
            .header(authHeader.getName(), authHeader.getValues().getFirst())
            .bodyValue(new CreateMeal(null))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(MealView.class)
            .returnResult()
            .getResponseBody()
    ).id();
  }

}