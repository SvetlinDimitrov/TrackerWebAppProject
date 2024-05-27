package org.nutriGuideBuddy.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nutriGuideBuddy.domain.dto.user.JwtResponse;
import org.nutriGuideBuddy.domain.dto.user.UserCreate;
import org.nutriGuideBuddy.domain.dto.user.UserDto;
import org.nutriGuideBuddy.domain.dto.user.UserView;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Naming convention follows the Given-When-Then structure, which is commonly used in behavior-driven development (BDD) to make tests more descriptive and easier to understand.
 **/
@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Testcontainers
class UserControllerIntegrationTest {

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
  void givenValidUserData_whenTestingCreateUser_thenUserIsSuccessfullyCreated() {

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
        .expectBody(JwtResponse.class)
        .value(user -> assertNotNull(user.userView().user().id()))
        .value(user -> assertEquals(newUser.username(), user.userView().user().username()))
        .value(user -> assertEquals(newUser.email(), user.userView().user().email()))
        .value(user -> assertNotNull(user.accessToken()))
        .value(user -> assertNotNull(user.accessToken().value()))
        .value(user -> assertNotNull(user.accessToken().expiresIn()));
  }

  @Test
  void givenInvalidUsername_whenTestingCreateUser_thenStatusBadRequest() {

    List<String> INVALID_USERNAMES = List.of(
        "",
        "        ",
        "Antidisestablishmentarianism's beauty may never be fully comprehended by the uninitiated, but those who delve deep into its labyrinthine depths will find themselves ensnared by its seductive allure, forever lost in a kaleidoscope of convoluted concepts and intricate ideologies that dance upon the precipice of comprehension."
    );

    for (String username : INVALID_USERNAMES) {
      webTestClient.post()
          .uri("/api/user")
          .bodyValue(createUser(
              username,
              Credentials.VALID_EMAIL.getValue(),
              Credentials.VALID_PASSWORD.getValue()
          ))
          .exchange()
          .expectStatus().isBadRequest();
    }
  }

  @Test
  void givenInvalidPassword_whenTestingCreateUser_thenStatusBadRequest() {

    List<String> INVALID_PASSWORDS = List.of(
        "Antidisestablishmentarianism's beauty may never be fully comprehended by the uninitiated, but those who delve deep into its labyrinthine depths will find themselves ensnared by its seductive allure, forever lost in a kaleidoscope of convoluted concepts and intricate ideologies that dance upon the precipice of comprehension.",
        "        ",
        "     .      ",
        "123",
        "12",
        "        ! "
    );

    for (String password : INVALID_PASSWORDS) {
      webTestClient.post()
          .uri("/api/user")
          .bodyValue(createUser(
              Credentials.VALID_USERNAME.getValue(),
              Credentials.VALID_EMAIL.getValue(),
              password
          ))
          .exchange()
          .expectStatus().isBadRequest();
    }
  }

  @Test
  void givenInvalidEmail_whenTestingCreateUser_thenStatusBadRequest() {

    List<String> INVALID_EMAILS = List.of(
        "",
        "asdd@asd@as123!",
        "     .      ",
        "sadasda@sdasdasdasdasd",
        "dasda@sd",
        "@sdad",
        "sada.sda@sdasdsd"
    );

    for (String email : INVALID_EMAILS) {
      webTestClient.post()
          .uri("/api/user")
          .bodyValue(createUser(
              Credentials.VALID_USERNAME.getValue(),
              email,
              Credentials.VALID_PASSWORD.getValue()
          ))
          .exchange()
          .expectStatus().isBadRequest();
    }
  }

  @Test
  void givenTwoUserWithTheSameEmail_whenTestingCreateUser_thenStatusBadRequest() {

    webTestClient.post()
        .uri("/api/user")
        .bodyValue(createUser(
            Credentials.VALID_USERNAME.getValue(),
            Credentials.VALID_EMAIL.getValue(),
            Credentials.VALID_PASSWORD.getValue()
        ))
        .exchange()
        .expectStatus().isCreated();

    webTestClient.post()
        .uri("/api/user")
        .bodyValue(createUser(
            Credentials.VALID_USERNAME.getValue(),
            Credentials.VALID_EMAIL.getValue(),
            Credentials.VALID_PASSWORD.getValue()
        ))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenTwoUserWithTheSameUsername_whenTestingCreateUser_thenShouldBeAllSuccessfullyCreated() {

    webTestClient.post()
        .uri("/api/user")
        .bodyValue(createUser(
            Credentials.VALID_USERNAME.getValue(),
            Credentials.VALID_EMAIL.getValue(),
            Credentials.VALID_PASSWORD.getValue()
        ))
        .exchange()
        .expectStatus().isCreated();

    webTestClient.post()
        .uri("/api/user")
        .bodyValue(createUser(
            Credentials.VALID_USERNAME.getValue(),
            "test2@abv.bg",
            Credentials.VALID_PASSWORD.getValue()
        ))
        .exchange()
        .expectStatus().isCreated();
  }

  @Test
  void givenNotProvidingBasicAuth_whenTestingGetUserView_thenServerShouldReturnStatus401() {

    webTestClient.get()
        .uri("/api/user")
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenProvidingBasicAuth_whenTestingGetUserView_thenServerShouldReturnStatusOkWithTheCorrectCredentials() {

    UserCreate newUser = createUser(
        Credentials.VALID_USERNAME.getValue(),
        Credentials.VALID_EMAIL.getValue(),
        Credentials.VALID_PASSWORD.getValue()
    );

    JwtResponse responseBody = webTestClient.post()
        .uri("/api/user")
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(JwtResponse.class)
        .value(user -> assertNotNull(user.userView().user().id()))
        .value(user -> assertEquals(newUser.username(), user.userView().user().username()))
        .value(user -> assertEquals(newUser.email(), user.userView().user().email()))
        .value(user -> assertNotNull(user.accessToken()))
        .value(user -> assertNotNull(user.accessToken().value()))
        .value(user -> assertNotNull(user.accessToken().expiresIn()))
        .returnResult()
        .getResponseBody();

    assert responseBody != null;
    webTestClient.get()
        .uri("/api/user")
        .header("Authorization", "Bearer " + responseBody.accessToken().value())
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserView.class)
        .value(user -> assertNotNull(user.id()))
        .value(user -> assertEquals(newUser.username(), user.username()))
        .value(user -> assertEquals(newUser.email(), user.email()));

  }

  @Test
  void givenValidUserCredentials_whenDeletingUser_thenServerShouldRemoveTheUserFromAuthAndDbReturning204() {

    UserCreate newUser = createUser(
        Credentials.VALID_USERNAME.getValue(),
        Credentials.VALID_EMAIL.getValue(),
        Credentials.VALID_PASSWORD.getValue()
    );

    JwtResponse responseBody = webTestClient.post()
        .uri("/api/user")
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(JwtResponse.class)
        .value(user -> assertNotNull(user.userView().user().id()))
        .value(user -> assertEquals(newUser.username(), user.userView().user().username()))
        .value(user -> assertEquals(newUser.email(), user.userView().user().email()))
        .value(user -> assertNotNull(user.accessToken()))
        .value(user -> assertNotNull(user.accessToken().value()))
        .value(user -> assertNotNull(user.accessToken().expiresIn()))
        .returnResult()
        .getResponseBody();

    assert responseBody != null;
    webTestClient.get()
        .uri("/api/user")
        .header("Authorization", "Bearer " + responseBody.accessToken().value())
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserView.class)
        .value(user -> assertNotNull(user.id()))
        .value(user -> assertEquals(newUser.username(), user.username()))
        .value(user -> assertEquals(newUser.email(), user.email()));

    webTestClient.delete()
        .uri("/api/user")
        .header("Authorization", "Bearer " + responseBody.accessToken().value())
        .exchange()
        .expectStatus().isNoContent()
        .returnResult(Void.class)
        .consumeWith(response -> {
          webTestClient.get()
              .uri("/api/user")
              .header("Authorization", "Bearer " + responseBody.accessToken().value())
              .exchange()
              .expectStatus().isUnauthorized();

          webTestClient.delete()
              .uri("/api/user")
              .header("Authorization", "Bearer " + responseBody.accessToken().value())
              .exchange()
              .expectStatus().isUnauthorized();
        });

  }

  @Test
  void givenValidUserCredentials_whenDeletingUserMultipleTimesInARowWithTheSameCredentials_thenServerShouldRemoveTheUserFromAuthAndDbReturning204AndThen401() {

    UserCreate newUser = createUser(
        Credentials.VALID_USERNAME.getValue(),
        Credentials.VALID_EMAIL.getValue(),
        Credentials.VALID_PASSWORD.getValue()
    );

    JwtResponse responseBody = webTestClient.post()
        .uri("/api/user")
        .bodyValue(newUser)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(JwtResponse.class)
        .value(user -> assertNotNull(user.userView().user().id()))
        .value(user -> assertEquals(newUser.username(), user.userView().user().username()))
        .value(user -> assertEquals(newUser.email(), user.userView().user().email()))
        .value(user -> assertNotNull(user.accessToken()))
        .value(user -> assertNotNull(user.accessToken().value()))
        .value(user -> assertNotNull(user.accessToken().expiresIn()))
        .returnResult()
        .getResponseBody();


    assert responseBody != null;
    webTestClient.delete()
        .uri("/api/user")
        .header("Authorization", "Bearer " + responseBody.accessToken().value())
        .exchange()
        .expectStatus().isNoContent()
        .returnResult(Void.class)
        .consumeWith(response -> {
          webTestClient.delete()
              .uri("/api/user")
              .header("Authorization", "Bearer " + responseBody.accessToken().value())
              .exchange()
              .expectStatus().isUnauthorized();

          webTestClient.delete()
              .uri("/api/user")
              .header("Authorization", "Bearer " + responseBody.accessToken().value())
              .exchange()
              .expectStatus().isUnauthorized();
        });

  }

  @Test
  void givenValidUserCredentialsAndAuth_whenModifyingUser_thenServerShouldReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    UserDto validUserUpdateCredentials = new UserDto("Pesho", "12345");

    webTestClient.patch()
        .uri("/api/user")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(validUserUpdateCredentials)
        .exchange()
        .expectStatus().isOk()
        .expectBody(JwtResponse.class)
        .value(user -> assertNotNull(user.userView().user().id()))
        .value(user -> assertEquals(validUserUpdateCredentials.username(), user.userView().user().username()))
        .value(user -> assertNotEquals(authHeader.getValues().getFirst(), user.accessToken().value()));


  }

  @Test
  void givenEmptyUserCredentialsAndValidAuth_whenModifyingUser_thenServerShouldReturnOk() {

    Header authHeader = setUpUserAndReturnAuthHeader();

    UserDto emptyCredentials = new UserDto(null, null);

    webTestClient.patch()
        .uri("/api/user")
        .header(authHeader.getName(), authHeader.getValues().getFirst())
        .bodyValue(emptyCredentials)
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void givenUserCredentialsWithoutAuth_whenModifyingUser_thenServerShouldReturn401() {

    UserDto emptyCredentials = new UserDto(null, null);

    webTestClient.patch()
        .uri("/api/user")
        .bodyValue(emptyCredentials)
        .exchange()
        .expectStatus().isUnauthorized();

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

  private UserCreate createUser(String username, String email, String password) {
    String token = jwtUtilEmailValidation.generateToken(email);
    return new UserCreate(username, email, password, token);
  }

  private UserCreate createUser(String username, String email, String password, String token) {
    return new UserCreate(username, email, password, token);
  }
}