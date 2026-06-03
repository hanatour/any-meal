package com.hanatour.anymeal;

import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.Locale;
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
        @RequestParam(required = false) String source,
        @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        if (StringUtils.isEmpty(x)) {
            x = anyMealConfig.getDefaultLongitude();
        }
        if (StringUtils.isEmpty(y)) {
            y = anyMealConfig.getDefaultLatitude();
        }
        source = resolveSource(source, acceptLanguage, x, y);
        String languageCode = resolveLanguageCode(acceptLanguage);
        log.debug("getRestaurantNear x:{}, y:{}, source:{}, languageCode:{}", x, y, source, languageCode);
        final var restaurant = anyMealService.getRestaurantNear(x, y, source, languageCode);
        logService.logLocation(x, y, restaurant);
        return restaurant
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }

    private static String resolveSource(String source, String acceptLanguage, String x, String y) {
        if (!StringUtils.isEmpty(source)) {
            return source;
        }
        if (!isSouthKoreaCoordinate(x, y)) {
            return "google";
        }
        if (isKoreanPreferred(acceptLanguage)) {
            return "kakao";
        }
        return "google";
    }

    private static boolean isSouthKoreaCoordinate(String x, String y) {
        try {
            double longitude = Double.parseDouble(x);
            double latitude = Double.parseDouble(y);
            return longitude >= 124.0 && longitude <= 132.0
                && latitude >= 33.0 && latitude <= 39.5;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static boolean isKoreanPreferred(String acceptLanguage) {
        if (StringUtils.isEmpty(acceptLanguage)) {
            return false;
        }
        String firstLanguage = acceptLanguage.split(",", 2)[0].trim().toLowerCase(Locale.ROOT);
        return firstLanguage.equals("ko") || firstLanguage.startsWith("ko-");
    }

    private static String resolveLanguageCode(String acceptLanguage) {
        if (StringUtils.isEmpty(acceptLanguage)) {
            return Locale.ENGLISH.toLanguageTag();
        }
        try {
            List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(acceptLanguage);
            if (!languageRanges.isEmpty()) {
                String range = languageRanges.getFirst().getRange();
                if (!range.equals("*")) {
                    return Locale.forLanguageTag(range).toLanguageTag();
                }
            }
        } catch (IllegalArgumentException e) {
            log.debug("Invalid Accept-Language header: {}", acceptLanguage);
        }
        String firstLanguage = acceptLanguage.split(",", 2)[0].trim();
        return firstLanguage.isEmpty() || firstLanguage.equals("*")
            ? Locale.ENGLISH.toLanguageTag()
            : Locale.forLanguageTag(firstLanguage).toLanguageTag();
    }

    @PostMapping("test/webhook")
    public String testWebhook(@RequestHeader Map<String, String> headers,
        @RequestBody String payload) {
        log.info("webhook headers: {}", headers);
        log.debug("webhook payload: {}", payload);
        return "Ok";
    }
}
