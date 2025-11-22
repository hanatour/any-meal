package com.hanatour.anymeal;

import com.hanatour.anymeal.geocalc.Point;
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

    @Test
    void getRestaurantNearTest() {
        final Optional<Restaurant> rest = anyMealService.getRestaurantNear("126.983618", "37.572043");
        System.out.println(rest);
        assertTrue(rest.isPresent());
    }

    @Test
    void getRestaurantNearAllPagesTest() {
        final List<Restaurant> rest = anyMealService.getRestaurantNearAllPages(
            Point.at("37.572043", "126.983618"), "distance", 100, "FD6", 1);
        System.out.println(rest);
        System.out.println(rest.size());
        assertFalse(rest.isEmpty());
    }

    @Test
    void getRestaurantNearDetailTest() {
        final CategoryResponse response = anyMealService.getRestaurantNear(
            Point.at("37.572043", "126.983618"), "distance", 100, "FD6", 3);
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void invalidPositionTest() {
        final List<Restaurant> rest = anyMealService.getRestaurantNearAllPages(
            Point.at("99.999999", "999.999999"), "distance", 500, "FD6", 1);
        System.out.println(rest);
        System.out.println(rest.size());
        assertFalse(rest.isEmpty());
    }

    @Test
    @DisplayName("깊은 산속에는 식당이 없다")
    void noRestaurantInMountain() {
        //https://www.google.com/maps/@38.3774456,128.1937431,11.88z
        final List<Restaurant> rest = anyMealService.getRestaurantNearAllPages(
            Point.at("38.3774456", "128.1937431"), "distance", 100, "FD6", 1);
        assertTrue(rest.isEmpty());
    }

}