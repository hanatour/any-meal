// Google Places 주변 검색으로 식당 추천을 제공하는 Source
package com.hanatour.anymeal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public final class GoogleRestaurantSearchSource implements RestaurantSearchSource {

    private static final String API_URL = "https://places.googleapis.com/v1/places:searchNearby";
    private static final String SOURCE_NAME = "google";
    private static final String FIELD_MASK = "places.id,places.displayName,places.formattedAddress,"
        + "places.location,places.primaryTypeDisplayName,places.types,places.googleMapsUri";
    private static final int MAX_RESULT_COUNT = 20;
    private static final double SEARCH_RADIUS_METERS = 500.0;
    private static final double MAX_CENTER_JITTER_METERS = 500.0;
    private static final double EARTH_RADIUS_METERS = 6_371_000.0;

    private final RestTemplate restTemplate;
    private final String apiKey;

    @Autowired
    public GoogleRestaurantSearchSource(@Value("${anymeal.google-api-key:}") String apiKey) {
        this(new RestTemplate(), apiKey);
    }

    GoogleRestaurantSearchSource(RestTemplate restTemplate, String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey != null ? apiKey.trim() : "";
    }

    @Override
    public Optional<Restaurant> searchNear(String x, String y) {
        if (apiKey.isEmpty()) {
            log.debug("Google API key not set, skipping");
            return Optional.empty();
        }
        try {
            double longitude = Double.parseDouble(x);
            double latitude = Double.parseDouble(y);
            Center searchCenter = jitterCenter(latitude, longitude);
            SearchNearbyRequest request = new SearchNearbyRequest(
                List.of("restaurant"),
                MAX_RESULT_COUNT,
                new LocationRestriction(
                    new Circle(
                        searchCenter,
                        SEARCH_RADIUS_METERS
                    )
                ),
                RankPreference.DISTANCE
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", apiKey);
            headers.set("X-Goog-FieldMask", FIELD_MASK);

            HttpEntity<SearchNearbyRequest> entity = new HttpEntity<>(request, headers);
            var response = restTemplate.postForEntity(API_URL, entity, GoogleNearbySearchResponse.class);
            GoogleNearbySearchResponse body = response.getBody();
            if (body == null || body.places() == null || body.places().isEmpty()) {
                return Optional.empty();
            }

            List<GooglePlace> places = body.places().stream()
                .filter(place -> place.displayName() != null
                    && place.displayName().text() != null
                    && !place.displayName().text().isBlank())
                .toList();
            if (places.isEmpty()) {
                return Optional.empty();
            }

            GooglePlace place = places.get(ThreadLocalRandom.current().nextInt(places.size()));
            return Optional.of(toRestaurant(place));
        } catch (Exception e) {
            log.warn("Google nearby search failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static Restaurant toRestaurant(GooglePlace place) {
        String placeName = textOrEmpty(place.displayName() != null ? place.displayName().text() : null);
        String formattedAddress = textOrEmpty(place.formattedAddress());
        String link = textOrEmpty(place.googleMapsUri());
        String id = textOrEmpty(place.id());
        if (id.isEmpty()) {
            id = placeName.isEmpty() ? "google-place" : "google-" + placeName.hashCode();
        }
        String categoryName = textOrEmpty(place.primaryTypeDisplayName() != null ? place.primaryTypeDisplayName().text() : null);
        if (categoryName.isEmpty() && place.types() != null && !place.types().isEmpty()) {
            categoryName = String.join(" > ", place.types());
        }
        String x = numberOrEmpty(place.location() != null ? place.location().longitude() : null);
        String y = numberOrEmpty(place.location() != null ? place.location().latitude() : null);

        return new Restaurant(
            formattedAddress,
            null,
            "음식점",
            categoryName,
            null,
            id,
            null,
            placeName,
            link,
            formattedAddress,
            x,
            y,
            SOURCE_NAME
        );
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private static String numberOrEmpty(Double value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static Center jitterCenter(double latitude, double longitude) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        double distance = MAX_CENTER_JITTER_METERS * Math.sqrt(random.nextDouble());
        double bearing = random.nextDouble(Math.PI * 2.0);
        double angularDistance = distance / EARTH_RADIUS_METERS;
        double latitudeRadians = Math.toRadians(latitude);
        double longitudeRadians = Math.toRadians(longitude);

        double jitteredLatitude = Math.asin(Math.sin(latitudeRadians) * Math.cos(angularDistance)
            + Math.cos(latitudeRadians) * Math.sin(angularDistance) * Math.cos(bearing));
        double jitteredLongitude = longitudeRadians + Math.atan2(
            Math.sin(bearing) * Math.sin(angularDistance) * Math.cos(latitudeRadians),
            Math.cos(angularDistance) - Math.sin(latitudeRadians) * Math.sin(jitteredLatitude)
        );

        return new Center(Math.toDegrees(jitteredLatitude), Math.toDegrees(jitteredLongitude));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GoogleNearbySearchResponse(
        @JsonProperty("places") List<GooglePlace> places
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GooglePlace(
        @JsonProperty("id") String id,
        @JsonProperty("displayName") GoogleDisplayName displayName,
        @JsonProperty("formattedAddress") String formattedAddress,
        @JsonProperty("location") GoogleLocation location,
        @JsonProperty("primaryTypeDisplayName") GoogleDisplayName primaryTypeDisplayName,
        @JsonProperty("types") List<String> types,
        @JsonProperty("googleMapsUri") String googleMapsUri
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GoogleDisplayName(
        @JsonProperty("text") String text,
        @JsonProperty("languageCode") String languageCode
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GoogleLocation(
        @JsonProperty("latitude") Double latitude,
        @JsonProperty("longitude") Double longitude
    ) {
    }

    record SearchNearbyRequest(
        @JsonProperty("includedTypes") List<String> includedTypes,
        @JsonProperty("maxResultCount") int maxResultCount,
        @JsonProperty("locationRestriction") LocationRestriction locationRestriction,
        @JsonProperty("rankPreference") RankPreference rankPreference
    ) {
    }

    record LocationRestriction(
        @JsonProperty("circle") Circle circle
    ) {
    }

    record Circle(
        @JsonProperty("center") Center center,
        @JsonProperty("radius") double radius
    ) {
    }

    record Center(
        @JsonProperty("latitude") double latitude,
        @JsonProperty("longitude") double longitude
    ) {
    }

    enum RankPreference {
        DISTANCE
    }
}
