package org.trackerwebapp.trackerwebapp.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.trackerwebapp.trackerwebapp.domain.dto.record.CreateRecord;
import org.trackerwebapp.trackerwebapp.domain.dto.record.RecordView;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserCreate;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserDetailsDto;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserDetailsView;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserView;
import org.trackerwebapp.trackerwebapp.domain.enums.Gender;
import org.trackerwebapp.trackerwebapp.domain.enums.WorkoutState;
import org.trackerwebapp.trackerwebapp.enums.Credentials;
import org.trackerwebapp.trackerwebapp.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Base64;

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
        .bodyValue(new CreateRecord(null, null))
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

  //TODO: TEST DISTRIBUTED MACROS
  //TODO: TEST GOALS
  //TODO: TEST CUSTOM NUTRITION VALUES
  //TODO: TEST IF ALL ALLOWED_NUTRIENTS ARE RETURNED WITH THE CORRECT VALUES
  //TODO: TEST CALORIES CONSUMED WHEN ADDING FOOD
  //TODO: TEST NUTRIENTS CONSUMED WHEN ADDING FOOD

  private UserCreate createUser(String username, String email, String password) {
    return new UserCreate(username, email, password);
  }

  private UserDetailsDto createDetails(BigDecimal kilograms, BigDecimal height, Integer age, WorkoutState workoutState, Gender gender) {
    return new UserDetailsDto(kilograms, height, age, workoutState, gender);
  }
}