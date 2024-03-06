package com.hanatour.anymeal;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AnyMealController {

    private final AnyMealService anyMealService;

    public AnyMealController(AnyMealService anyMealService) {
        this.anyMealService = anyMealService;
    }

    @GetMapping("restaurant/near")
    public Optional<Restaurant> getRestaurantNear(
        @RequestParam(defaultValue = "126.983618") String x,
        @RequestParam(defaultValue = "37.572043") String y) {
        log.debug("getRestaurantNear x:{}, y:{}", x, y);
        return anyMealService.getRestaurantNear(x, y);
    }

}
