package com.hanatour.anymeal;

import com.hanatour.anymeal.geocalc.Point;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnyMealServiceTest {

  @Autowired
  AnyMealService anyMealService;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getRestaurantNearTest() {
    Optional<Restaurant> rest = anyMealService.getRestaurantNear("126.983618", "37.572043");
    System.out.println(rest);
    assertTrue(rest.isPresent());
  }

  @Test
  void getRestaurantNearAllPagesTest() {
    List<Restaurant> rest = anyMealService.getRestaurantNearAllPages(Point.at("37.572043", "126.983618"), "distance", 100, "FD6", 1);
    System.out.println(rest);
    System.out.println(rest.size());
  }

  @Test
  void getRestaurantNearDetailTest() {
    CategoryResponse response = anyMealService.getRestaurantNear(Point.at("37.572043", "126.983618"), "distance", 100, "FD6", 3);
    System.out.println(response);
  }

  @Test
  void invalidPositionTest() {
    List<Restaurant> rest = anyMealService.getRestaurantNearAllPages(Point.at("99.999999", "999.999999"), "distance", 500, "FD6", 1);
    System.out.println(rest);
    System.out.println(rest.size());
  }

  @Test
  @DisplayName("깊은 산속에는 식당이 없다")
  void noRestaurantInMountain() {
    //https://www.google.com/maps/@38.3774456,128.1937431,11.88z
    List<Restaurant> rest = anyMealService.getRestaurantNearAllPages(Point.at("38.3774456", "128.1937431"), "distance", 100, "FD6", 1);
    assertTrue(rest.isEmpty());
  }

}