package com.hanatour.anymeal;

import java.net.http.HttpHeaders;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AnyMealController {

    private final AnyMealConfig  anyMealConfig;
    private final AnyMealService anyMealService;
    private final LogService logService;

    public AnyMealController(AnyMealConfig anyMealConfig, AnyMealService anyMealService, LogService logService) {
        this.anyMealConfig = anyMealConfig;
        this.anyMealService = anyMealService;
        this.logService = logService;
    }

    @GetMapping("restaurant/near")
    public Optional<Restaurant> getRestaurantNear(
        @RequestParam(defaultValue = "126.983618") String x,
        @RequestParam(defaultValue = "37.572043") String y) {
        log.debug("getRestaurantNear x:{}, y:{}", x, y);
        final Optional<Restaurant> restaurant = anyMealService.getRestaurantNear(x, y);
        logService.logLocation(x, y, restaurant);
        return restaurant;
    }

    @PostMapping("test/webhook")
    public String testWebhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        System.out.println(headers);
        System.out.println("===================================================================================");
        System.out.println(payload);
        return "Ok";
    }
}
