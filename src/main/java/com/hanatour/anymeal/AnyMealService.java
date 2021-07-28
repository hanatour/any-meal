package com.hanatour.anymeal;

import com.hanatour.anymeal.geocalc.Coordinate;
import com.hanatour.anymeal.geocalc.EarthCalc;
import com.hanatour.anymeal.geocalc.Point;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnyMealService {

  private AnyMealService() {
  }

  private static final RestTemplate restTemplate = new RestTemplate();

  /**
   * 랜덤으로 근처의 식당을 하나 추천해준다. 카카오 API를 이용하여 구현.
   * @return 식당 정보
   */
  public static Optional<Restaurant> getRestaurantNear(String x, String y) {
    Coordinate lat = Coordinate.fromDegrees(Double.parseDouble(y));
    Coordinate lng = Coordinate.fromDegrees(Double.parseDouble(x));
    Point orgPoint = Point.at(lat, lng);
    Point point1 = EarthCalc.gcd.pointAt(orgPoint, 0., 90.);
    Point point2 = EarthCalc.gcd.pointAt(orgPoint, 90., 90.);
    Point point3 = EarthCalc.gcd.pointAt(orgPoint, 180., 90.);
    Point point4 = EarthCalc.gcd.pointAt(orgPoint, 270., 90.);
    List<Point> points = Arrays.asList(point1, point2, point3, point4);
    List<Restaurant> restaurants =
            points.stream()
                    .parallel()
                    .flatMap(p -> getRestaurantNearAllPages(p, "distance", 100, "FD6", 1).stream())
                    .distinct()
                    .collect(Collectors.toList());
    System.out.println(restaurants.size());
    try {
      int index = Math.abs(SecureRandom.getInstanceStrong().nextInt()) % restaurants.size();
      return Optional.of(restaurants.get(index));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return restaurants.stream().findAny();
    }
  }

  static List<Restaurant> getRestaurantNearAllPages(Point point, String sort, int radius, String categoryCode, int page) {
    CategoryResponse categoryResponse = getRestaurantNear(point, sort, radius, categoryCode, page);
    if (categoryResponse == null) {
      return Collections.emptyList();
    }
    final List<Restaurant> restaurants = categoryResponse.getRestaurants();
    if (categoryResponse.getMeta().getEnd()) {
      return restaurants;
    }
    restaurants.addAll(getRestaurantNearAllPages(point, sort, radius, categoryCode, page + 1));
    return restaurants;
  }

  static CategoryResponse getRestaurantNear(Point point, String sort, int radius, String categoryCode, int page){
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK 39d7fcaff32cd31ddae76c988072fa72");
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    String url = "https://dapi.kakao.com/v2/local/search/category.json";
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("x", point.longitude)
            .queryParam("y", point.latitude)
            .queryParam("sort", sort)
            .queryParam("radius", radius)
            .queryParam("category_group_code", categoryCode)
            .queryParam("page", page);

    HttpEntity<CategoryResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, CategoryResponse.class);

    return response.getBody();
  }

}
