package org.nutriGuideBuddy.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nutriGuideBuddy.domain.dto.meal.InsertFoodDto;
import org.nutriGuideBuddy.domain.dto.user.JwtResponse;
import org.nutriGuideBuddy.domain.dto.user.UserCreate;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.util.Objects;


@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Testcontainers
class NutritionixApiControllerTest {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JWTUtilEmailValidation jwtUtilEmailValidation;

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
  void givenNoAuth_whenTestingGetFoodBySearchCriteria_thenServerMustReturnUnauthorized() {

    webTestClient
        .get()
        .uri("/api/food_db_api/search/common/" + Credentials.VALID_FOOD_TO_SEARCH.getValue())
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenValidAuth_whenTestingGetFoodBySearchCriteria_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .get()
        .uri("/api/food_db_api/search/common/" + Credentials.VALID_FOOD_TO_SEARCH.getValue())
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void givenValidAuth_whenTestingGetFoodBySearchCriteriaAndThenInsertingItIntoUserCustomFoods_thenServerMustReturnOkAndCreated() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    InsertFoodDto food = Objects.requireNonNull(webTestClient
            .get()
            .uri("/api/food_db_api/search/common/" + Credentials.VALID_FOOD_TO_SEARCH.getValue())
            .header(authHeader.getName(), authHeader.getValues().getFirst())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(InsertFoodDto.class)
            .returnResult()
            .getResponseBody())
        .getFirst();

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(food)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Void.class);
  }

  @Test
  void givenNoAuth_whenTestingGetBrandedFoodById_thenServerMustReturnUnauthorized() {

    webTestClient
        .get()
        .uri("/api/food_db_api/search/branded/" + Credentials.VALID_FOOD_BRANDED_ID.getValue())
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenValidAuth_whenTestingGetBrandedFoodById_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .get()
        .uri("/api/food_db_api/search/branded/" + Credentials.VALID_FOOD_BRANDED_ID.getValue())
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void givenValidAuth_whenTestingGetBrandedFoodByIdAndThenInsertingItIntoUserCustomFoods_thenServerMustReturnOkAndCreated() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    InsertFoodDto food = Objects.requireNonNull(webTestClient
            .get()
            .uri("/api/food_db_api/search/branded/" + Credentials.VALID_FOOD_BRANDED_ID.getValue())
            .header(authHeader.getName(), authHeader.getValues().getFirst())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(InsertFoodDto.class)
            .returnResult()
            .getResponseBody())
        .getFirst();

    webTestClient
        .post()
        .uri("/api/custom/food")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(food)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Void.class);
  }

  @Test
  void givenNoAuth_whenTestingGetFoodsByName_thenServerMustReturnUnauthorized() {

    webTestClient
        .get()
        .uri("/api/food_db_api/search")
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenValidAuth_whenTestingGetFoodsByName_thenServerMustReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient
        .get()
        .uri(UriComponentsBuilder.fromPath("/api/food_db_api/search")
            .queryParam("foodName", Credentials.VALID_FOOD_NAME.getValue())
            .build().toString())
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk();
  }

//  @Test
//  void givenValidAuth_whenTestingGetMultipleFoodsByNameThenByBrandedIdAndCommonNameAndAddingThenToTheClient_thenServerMustReturnOk() {
//
//    Header authHeader = setUpUserAndReturnAuthHeader();
//    List<String> commonFoods = new ArrayList<>();
//    List<String> brandedFoods = new ArrayList<>();
//
//    getFoodsToSearch()
//        .forEach(VALID_FOOD_NAME -> {
//          ListFoodsResponse allFoods = webTestClient
//              .get()
//              .uri(UriComponentsBuilder.fromPath("/api/food_db_api/search")
//                  .queryParam("foodName", Credentials.VALID_FOOD_NAME.getValue())
//                  .build().toString())
//              .header(authHeader.getName(), authHeader.getValues().getFirst())
//              .exchange()
//              .expectStatus().isOk()
//              .expectBody(ListFoodsResponse.class)
//              .returnResult().getResponseBody();
//
//          assert allFoods != null;
//          commonFoods.addAll(allFoods.getCommon().stream().map(CommandFoodShortenDto::getFoodName).toList());
//          brandedFoods.addAll(allFoods.getBranded().stream().map(BrandedFoodShortenDto::getItemId).toList());
//        });
//
//    List<InsertFoodDto> insertFoodDtos = new ArrayList<>();
//
//    commonFoods.forEach(
//        commonFood -> {
//          InsertFoodDto food = Objects.requireNonNull(webTestClient
//                  .get()
//                  .uri("/api/food_db_api/search/common/" + commonFood)
//                  .header(authHeader.getName(), authHeader.getValues().getFirst())
//                  .exchange()
//                  .expectStatus().isOk()
//                  .expectBodyList(InsertFoodDto.class)
//                  .returnResult()
//                  .getResponseBody())
//              .getFirst();
//
//          insertFoodDtos.add(food);
//        }
//    );
//
//    brandedFoods.forEach(
//        brandedFoodId -> {
//          InsertFoodDto food = Objects.requireNonNull(webTestClient
//                  .get()
//                  .uri("/api/food_db_api/search/branded/" +brandedFoodId)
//                  .header(authHeader.getName(), authHeader.getValues().getFirst())
//                  .exchange()
//                  .expectStatus().isOk()
//                  .expectBodyList(InsertFoodDto.class)
//                  .returnResult()
//                  .getResponseBody())
//              .getFirst();
//
//          insertFoodDtos.add(food);
//        }
//    );
//
//    insertFoodDtos.forEach(
//        insertFoodDto -> {
//          webTestClient
//              .post()
//              .uri("/api/custom/food")
//              .header(authHeader.getName(), authHeader.getValues().getFirst())
//              .bodyValue(insertFoodDto)
//              .exchange()
//              .expectStatus().isCreated()
//              .expectBody(Void.class);
//        }
//    );
//
//  }

  private Header setUpUserAndReturnAuthHeader() {
    String token = jwtUtilEmailValidation.generateToken(Credentials.VALID_EMAIL.getValue());

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