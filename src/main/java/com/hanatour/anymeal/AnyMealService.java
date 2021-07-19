package com.hanatour.anymeal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AnyMealService {

  private AnyMealService() {
  }

  private static final RestTemplate restTemplate = new RestTemplate();

  /**
   * 랜덤으로 근처의 식당을 하나 추천해준다. 카카오 API를 이용하여 구현.
   * @return 식당 정보
   */
  public static Optional<Restaurant> getRestaurantNear(String x, String y) {
    List<Restaurant> restaurants = getRestaurantNear(x, y, "distance", "500", "FD6", 1);
    return restaurants.stream().findFirst();
  }

  private static List<Restaurant> getRestaurantNear(String x, String y, String sort, String radius, String categoryCode, int page){
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK 39d7fcaff32cd31ddae76c988072fa72");
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    String url = "https://dapi.kakao.com/v2/local/search/category.json";
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("x", x)
            .queryParam("y", y)
            .queryParam("sort", sort)
            .queryParam("radius", radius)
            .queryParam("category_group_code", categoryCode)
            .queryParam("page", page);

    HttpEntity<Documents> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Documents.class);
    if (response.getBody() == null) {
      return Collections.emptyList();
    }
    return response.getBody().getDocuments();
  }

}
