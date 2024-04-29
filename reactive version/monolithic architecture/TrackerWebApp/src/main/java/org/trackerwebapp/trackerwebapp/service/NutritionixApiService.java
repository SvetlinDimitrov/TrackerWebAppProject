package org.trackerwebapp.trackerwebapp.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.CalorieView;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.InsertFoodDto;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.NutritionView;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.ServingView;
import org.trackerwebapp.trackerwebapp.domain.dto.nutritionxApi.FoodItem;
import org.trackerwebapp.trackerwebapp.domain.enums.AllowedCalorieUnits;
import org.trackerwebapp.trackerwebapp.utils.nutritionix_api.GetFoodsResponse;
import org.trackerwebapp.trackerwebapp.utils.nutritionix_api.NutrientMapperUtils;
import org.trackerwebapp.trackerwebapp.utils.nutritionix_api.ServingMapperUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NutritionixApiService {

  private static final int BUFFER_SIZE = 64 * 1024 * 1024;
  @Value("${api.url}")
  public String BASE_URL;
  @Value("${api.id}")
  public String X_API_ID;
  @Value("${api.key}")
  public String X_API_KEY;
  private WebClient webClient;

  @PostConstruct
  private void init() {
    WebClient.Builder webClientBuilder =
        WebClient.builder()
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(BUFFER_SIZE))
                    .build())
            .baseUrl(BASE_URL)
            .defaultHeader("x-app-id", X_API_ID)
            .defaultHeader("x-app-key", X_API_KEY);
    webClient = webClientBuilder.build();
  }

  public Mono<InsertFoodDto> getFoodBySearchTerm(String query) {
    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("query", query);

    return webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/v2/natural/nutrients").build())
        .contentType(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .body(BodyInserters.fromValue(requestBody))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, this::handle400Response)
//        .onStatus(HttpStatusCode::is5xxServerError, this::handle500Response)
        .bodyToMono(GetFoodsResponse.class)
        .map(GetFoodsResponse::getFoods)
        .map(this::toView);
  }

  private InsertFoodDto toView(List<FoodItem> items) {
    FoodItem item = items.getFirst();
    CalorieView calorieView = new CalorieView(BigDecimal.valueOf(item.getNfCalories()), AllowedCalorieUnits.CALORIE.getSymbol());
    List<NutritionView> nutrients = NutrientMapperUtils.getNutrients(item);
    List<ServingView> servings = ServingMapperUtils.getServings(item);
    return new InsertFoodDto(item.getFoodName(), calorieView, servings, nutrients);
  }
  private Mono<? extends Throwable> handle400Response(ClientResponse response) {
    return response.bodyToMono(BadRequestException.class)
        .flatMap(Mono::error);
  }
}
