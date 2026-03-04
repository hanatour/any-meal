package com.hanatour.anymeal;

import io.micrometer.common.util.StringUtils;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AnyMealController {

    private final AnyMealConfig anyMealConfig;
    private final AnyMealService anyMealService;
    private final LogService logService;

    public AnyMealController(AnyMealConfig anyMealConfig, AnyMealService anyMealService,
        LogService logService) {
        this.anyMealConfig = anyMealConfig;
        this.anyMealService = anyMealService;
        this.logService = logService;
    }

    @GetMapping("restaurant/near")
    public ResponseEntity<Restaurant> getRestaurantNear(
        @RequestParam(required = false) String x,
        @RequestParam(required = false) String y,
        @RequestParam(required = false, defaultValue = "kakao") String source) {
        log.debug("getRestaurantNear x:{}, y:{}, source:{}", x, y, source);
        if (StringUtils.isEmpty(x)) {
            x = anyMealConfig.getDefaultLongitude();
        }
        if (StringUtils.isEmpty(y)) {
            y = anyMealConfig.getDefaultLatitude();
        }
        final var restaurant = anyMealService.getRestaurantNear(x, y, source);
        logService.logLocation(x, y, restaurant);
        return restaurant
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("test/webhook")
    public String testWebhook(@RequestHeader Map<String, String> headers,
        @RequestBody String payload) {
        log.info("webhook headers: {}", headers);
        log.debug("webhook payload: {}", payload);
        return "Ok";
    }
}
