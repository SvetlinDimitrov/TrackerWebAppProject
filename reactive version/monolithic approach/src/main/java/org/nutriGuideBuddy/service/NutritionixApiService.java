package org.nutriGuideBuddy.service;

import jakarta.annotation.PostConstruct;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.meal.*;
import org.nutriGuideBuddy.domain.dto.nutritionxApi.FoodItem;
import org.nutriGuideBuddy.domain.dto.nutritionxApi.ListFoodsResponse;
import org.nutriGuideBuddy.domain.enums.AllowedCalorieUnits;
import org.nutriGuideBuddy.utils.nutritionix_api.FoodInfoMapperUtils;
import org.nutriGuideBuddy.utils.nutritionix_api.GetFoodsResponse;
import org.nutriGuideBuddy.utils.nutritionix_api.NutrientMapperUtils;
import org.nutriGuideBuddy.utils.nutritionix_api.ServingMapperUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
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

  public Mono<List<InsertFoodDto>> getCommonFoodBySearchTerm(String query) {
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
        .onStatus(HttpStatusCode::is5xxServerError, this::handle500Response)
        .bodyToMono(GetFoodsResponse.class)
        .map(GetFoodsResponse::getFoods)
        .map(list -> list.stream()
            .map(this::toView)
            .toList());
  }

  public Mono<List<InsertFoodDto>> getBrandedFoodById(String id) {

    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/v2/search/item")
            .queryParam("nix_item_id" , id).build())
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, this::handle400Response)
        .onStatus(HttpStatusCode::is5xxServerError, this::handle500Response)
        .bodyToMono(GetFoodsResponse.class)
        .map(GetFoodsResponse::getFoods)
        .map(list -> list.stream()
            .map(this::toView)
            .toList());
  }

  public Mono<ListFoodsResponse> getAllFoodsByFoodName(String foodName) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/v2/search/instant/")
            .queryParam("query" , foodName).build())
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, this::handle400Response)
        .onStatus(HttpStatusCode::is5xxServerError, this::handle500Response)
        .bodyToMono(ListFoodsResponse.class);
  }

  private InsertFoodDto toView(FoodItem item) {
    CalorieView calorieView = new CalorieView(BigDecimal.valueOf(item.getNfCalories()), AllowedCalorieUnits.CALORIE.getSymbol());
    FoodInfoView foodInfoView = FoodInfoMapperUtils.generateFoodInfo(item);
    List<NutritionView> nutrients = NutrientMapperUtils.getNutrients(item);
    List<ServingView> servings = ServingMapperUtils.getServings(item);
    ServingView mainServing = ServingMapperUtils.getMainServing(item);
    return new InsertFoodDto(item.getFoodName(), calorieView, mainServing, foodInfoView, servings, nutrients);
  }
  private Mono<? extends Throwable> handle400Response(ClientResponse response) {
    if(response.statusCode().equals(HttpStatusCode.valueOf(404))){
      return Mono.error(new BadRequestException("Not found"));
    }
    if (response.statusCode().equals(HttpStatusCode.valueOf(401))) {
      return Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(429)));
    }
    return response.bodyToMono(BadRequestException.class)
        .flatMap(Mono::error);
  }
  private Mono<? extends Throwable> handle500Response(ClientResponse response) {
    return response.bodyToMono(BadRequestException.class)
        .flatMap(Mono::error);
  }
}
