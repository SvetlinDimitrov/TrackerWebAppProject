package org.nutriGuideBuddy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class TrackerWebAppApplicationTests {

	@Container
	public static GenericContainer<?> mysqlContainer = new GenericContainer<>("mysql:latest")
			.withExposedPorts(3306)
			.withEnv("MYSQL_ROOT_PASSWORD", "12345")
			.withEnv("MYSQL_DATABASE", "reactiveDB");

	@BeforeAll
	static void beforeAll() {
		mysqlContainer.start();
	}

	@DynamicPropertySource
	static void setDatasourceProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.r2dbc.url", () -> "r2dbc:mysql://" + mysqlContainer.getHost() + ":" + mysqlContainer.getFirstMappedPort() + "/reactiveDB");
		registry.add("spring.liquibase.url", () -> "jdbc:mysql://" + mysqlContainer.getHost() + ":" + mysqlContainer.getFirstMappedPort() + "/reactiveDB");
	}

	@Test
	void contextLoads() {
	}

}
