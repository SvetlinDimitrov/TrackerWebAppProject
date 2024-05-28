package org.nutriGuideBuddy.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nutriGuideBuddy.domain.dto.user.JwtResponse;
import org.nutriGuideBuddy.domain.dto.user.UserCreate;
import org.nutriGuideBuddy.domain.dto.user.UserDetailsDto;
import org.nutriGuideBuddy.domain.dto.user.UserDetailsView;
import org.nutriGuideBuddy.domain.enums.Gender;
import org.nutriGuideBuddy.domain.enums.WorkoutState;
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

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("secret")
@Testcontainers
class UserDetailsControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JWTUtilEmailValidation jwtUtilEmailValidation;

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
  void givenValidAuth_whenGetUserDetails_thenServerShouldReturnUserDetailsAndStatusOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    webTestClient.get()
        .uri("/api/user/details")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .value(detailsView -> assertNull(detailsView.age()))
        .value(detailsView -> assertNull(detailsView.height()))
        .value(detailsView -> assertNull(detailsView.workoutState()))
        .value(detailsView -> assertNull(detailsView.kilograms()))
        .value(detailsView -> assertNull(detailsView.gender()));
  }

  @Test
  void givenNoAuth_whenGetUserDetails_thenServerShouldReturn401() {

    webTestClient.get()
        .uri("/api/user/details")
        .exchange()
        .expectStatus().isUnauthorized();

  }

  @Test
  void givenNoAuth_whenModifyUserDetails_thenServerShouldReturn401() {

    UserDetailsDto validDetails = createDetails(
        new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
        new BigDecimal(Credentials.VALID_DETAIL_HEIGHT.getValue()),
        Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
        WorkoutState.valueOf(Credentials.VALID_DETAIL_WORKOUT.getValue()),
        Gender.valueOf(Credentials.VALID_DETAIL_GENDER.getValue())
    );

    webTestClient.patch()
        .uri("/api/user/details")
        .bodyValue(validDetails)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenValidAuth_whenModifyUserDetailsWithValidCredentials_thenServerShouldReturnUserDetailsAndStatusOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    UserDetailsDto validDetails = createDetails(
        new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
        new BigDecimal(Credentials.VALID_DETAIL_HEIGHT.getValue()),
        Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
        WorkoutState.valueOf(Credentials.VALID_DETAIL_WORKOUT.getValue()),
        Gender.valueOf(Credentials.VALID_DETAIL_GENDER.getValue())
    );

    webTestClient.patch()
        .uri("/api/user/details")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(validDetails)
        .exchange()
        .expectStatus().isOk()
        .expectBody(JwtResponse.class)
        .value(detailsView -> assertEquals(validDetails.gender(), detailsView.userView().userDetails().gender()))
        .value(detailsView -> assertEquals(0, validDetails.height().compareTo(detailsView.userView().userDetails().height())))
        .value(detailsView -> assertEquals(0, validDetails.kilograms().compareTo(detailsView.userView().userDetails().kilograms())))
        .value(detailsView -> assertEquals(validDetails.age(), detailsView.userView().userDetails().age()))
        .value(detailsView -> assertEquals(validDetails.workoutState(), detailsView.userView().userDetails().workoutState()));
  }

  @Test
  void givenValidAuth_whenModifyPartOfUserDetailsWithValidCredentials_thenServerShouldReturnUserDetailsAndStatusOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    UserDetailsDto validDetails = createDetails(
        new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
        null,
        Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
        null,
        null
    );

    webTestClient.patch()
        .uri("/api/user/details")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(validDetails)
        .exchange()
        .expectStatus().isOk()
        .expectBody(JwtResponse.class)
        .value(detailsView -> assertNull(detailsView.userView().userDetails().gender()))
        .value(detailsView -> assertNull(detailsView.userView().userDetails().workoutState()))
        .value(detailsView -> assertNull(detailsView.userView().userDetails().height()))
        .value(detailsView -> assertEquals(0, validDetails.kilograms().compareTo(detailsView.userView().userDetails().kilograms())))
        .value(detailsView -> assertEquals(validDetails.age(), detailsView.userView().userDetails().age()));
  }

  @Test
  void givenValidAuth_whenModifyUserDetailsWithInvalidKilograms_thenServerShouldReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    List<String> INVALID_KILOGRAMS = List.of(
        "-1",
        "0",
        "-2313"
    );

    for (String invalidKilogram : INVALID_KILOGRAMS) {
      UserDetailsDto validDetails = createDetails(
          new BigDecimal(invalidKilogram),
          new BigDecimal(Credentials.VALID_DETAIL_HEIGHT.getValue()),
          Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
          WorkoutState.valueOf(Credentials.VALID_DETAIL_WORKOUT.getValue()),
          Gender.valueOf(Credentials.VALID_DETAIL_GENDER.getValue())
      );

      webTestClient.patch()
          .uri("/api/user/details")
          .header(authHeader.getName(), authHeader.getValues().getFirst())
          .bodyValue(validDetails)
          .exchange()
          .expectStatus().isBadRequest();
    }
  }

  @Test
  void givenValidAuth_whenModifyUserDetailsWithInvalidHeight_thenServerShouldReturnBadRequest() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    List<String> INVALID_HEIGHT = List.of(
        "-1",
        "0",
        "-2313",
        "0.99",
        "11.1",
        "19.9"
    );

    for (String invalidHeight : INVALID_HEIGHT) {
      UserDetailsDto validDetails = createDetails(
          new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
          new BigDecimal(invalidHeight),
          Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
          WorkoutState.valueOf(Credentials.VALID_DETAIL_WORKOUT.getValue()),
          Gender.valueOf(Credentials.VALID_DETAIL_GENDER.getValue())
      );

      webTestClient.patch()
          .uri("/api/user/details")
          .header(authHeader.getName(), authHeader.getValues().getFirst())
          .bodyValue(validDetails)
          .exchange()
          .expectStatus().isBadRequest();
    }
  }

  @Test
  void givenValidAuth_whenModifyUserDetailsWithInvalidAge_thenServerShouldReturnBadRequest() throws InterruptedException {

    Header authHeader = setUpUserAndReturnAuthHeader();
    System.out.println(authHeader.getName());
    System.out.println(authHeader.getValues().getFirst());
    List<String> INVALID_AGES = List.of(
        "-1",
        "0",
        "-2313"
    );

    for (String invalidAge : INVALID_AGES) {
      UserDetailsDto validDetails = createDetails(
          new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
          new BigDecimal(Credentials.VALID_DETAIL_HEIGHT.getValue()),
          Integer.valueOf(invalidAge),
          WorkoutState.valueOf(Credentials.VALID_DETAIL_WORKOUT.getValue()),
          Gender.valueOf(Credentials.VALID_DETAIL_GENDER.getValue())
      );

      webTestClient.patch()
          .uri("/api/user/details")
          .header(authHeader.getName(), authHeader.getValues().getFirst())
          .bodyValue(validDetails)
          .exchange()
          .expectStatus().isBadRequest();
    }
  }

  @Test
  void givenValidAuth_whenModifyUserDetailsWithAllPossibleGenderTypes_thenServerShouldReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    for (Gender gender : Gender.values()) {
      UserDetailsDto validDetails = createDetails(
          new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
          new BigDecimal(Credentials.VALID_DETAIL_HEIGHT.getValue()),
          Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
          WorkoutState.valueOf(Credentials.VALID_DETAIL_WORKOUT.getValue()),
          gender
      );

      webTestClient.patch()
          .uri("/api/user/details")
          .header(authHeader.getName(), authHeader.getValues().getFirst())
          .bodyValue(validDetails)
          .exchange()
          .expectStatus().isOk()
          .expectBody(JwtResponse.class)
          .returnResult();
    }
  }

  @Test
  void givenValidAuth_whenModifyUserDetailsWithAllPossibleWorkoutTypes_thenServerShouldReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    for (WorkoutState workoutState : WorkoutState.values()) {
      UserDetailsDto validDetails = createDetails(
          new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
          new BigDecimal(Credentials.VALID_DETAIL_HEIGHT.getValue()),
          Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
          workoutState,
          Gender.valueOf(Credentials.VALID_DETAIL_GENDER.getValue())
      );

      webTestClient.patch()
          .uri("/api/user/details")
          .header(authHeader.getName(), authHeader.getValues().getFirst())
          .bodyValue(validDetails)
          .exchange()
          .expectStatus().isOk()
          .expectBody(JwtResponse.class)
          .returnResult();
    }
  }

  @Test
  void givenValidAuth_whenModifyUserDetailsAndGettingWithChangedValues_thenServerShouldReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    UserDetailsDto validDetails = createDetails(
        new BigDecimal(Credentials.VALID_DETAIL_KILOGRAMS.getValue()),
        new BigDecimal(Credentials.VALID_DETAIL_HEIGHT.getValue()),
        Integer.valueOf(Credentials.VALID_DETAIL_AGE.getValue()),
        WorkoutState.valueOf(Credentials.VALID_DETAIL_WORKOUT.getValue()),
        Gender.valueOf(Credentials.VALID_DETAIL_GENDER.getValue())
    );

    webTestClient.get()
        .uri("/api/user/details")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .value(detailsView -> assertNull(detailsView.age()))
        .value(detailsView -> assertNull(detailsView.workoutState()))
        .value(detailsView -> assertNull(detailsView.kilograms()))
        .value(detailsView -> assertNull(detailsView.height()))
        .returnResult();

    webTestClient.patch()
        .uri("/api/user/details")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(validDetails)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .returnResult();

    webTestClient.get()
        .uri("/api/user/details")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDetailsView.class)
        .value(detailsView -> assertEquals(validDetails.gender(), detailsView.gender()))
        .value(detailsView -> assertEquals(0, validDetails.height().compareTo(detailsView.height())))
        .value(detailsView -> assertEquals(0, validDetails.kilograms().compareTo(detailsView.kilograms())))
        .value(detailsView -> assertEquals(validDetails.age(), detailsView.age()))
        .value(detailsView -> assertEquals(validDetails.workoutState(), detailsView.workoutState()))
        .returnResult();
  }


  private UserDetailsDto createDetails(BigDecimal kilograms, BigDecimal height, Integer age, WorkoutState workoutState, Gender gender) {
    return new UserDetailsDto(kilograms, height, age, workoutState, gender);
  }

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