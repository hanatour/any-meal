package com.hanatour.anymeal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class AnyMealService {

  private AnyMealService() {
  }

  private static final RestTemplate restTemplate = new RestTemplate();

  /**
   * 랜덤으로 근처의 식당을 하나 추천해준다. 카카오 API를 이용하여 구현.
   * @return 식당 정보 JSON String
   */
  public static String getRestaurantNear() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK 39d7fcaff32cd31ddae76c988072fa72");
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    String url = "https://dapi.kakao.com/v2/local/search/category.json";
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
      .queryParam("x", "126.983618")
      .queryParam("y", "37.572043")
      .queryParam("sort", "distance")
      .queryParam("radius", "500")
      .queryParam("category_group_code", "FD6");

    HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
        entity, String.class);

    return response.getBody();
  }
}
