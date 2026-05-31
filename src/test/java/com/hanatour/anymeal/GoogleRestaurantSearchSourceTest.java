// Google Places 주변 검색 소스의 매핑과 호출 조건을 검증하는 테스트
package com.hanatour.anymeal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.POST;

class GoogleRestaurantSearchSourceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final double EARTH_RADIUS_METERS = 6_371_000.0;

    @Test
    @DisplayName("API 키가 없으면 Google 검색을 건너뛴다")
    void searchNear_returnsEmpty_whenApiKeyMissing() {
        GoogleRestaurantSearchSource source = new GoogleRestaurantSearchSource(new RestTemplate(), "");

        Optional<Restaurant> result = source.searchNear("127.0", "37.5");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Nearby Search 응답을 Restaurant로 매핑한다")
    void searchNear_mapsResponseToRestaurant() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        GoogleRestaurantSearchSource source = new GoogleRestaurantSearchSource(restTemplate, "test-key");
        double longitude = 127.0;
        double latitude = 37.5;

        server.expect(requestTo("https://places.googleapis.com/v1/places:searchNearby"))
            .andExpect(method(POST))
            .andExpect(header("X-Goog-Api-Key", "test-key"))
            .andExpect(header("X-Goog-FieldMask",
                "places.id,places.displayName,places.formattedAddress,places.location,places.primaryTypeDisplayName,places.types,places.googleMapsUri"))
            .andExpect(request -> {
                assertEquals(MediaType.APPLICATION_JSON, request.getHeaders().getContentType());
                String requestBody = ((ByteArrayOutputStream) request.getBody()).toString(StandardCharsets.UTF_8);
                JsonNode body = OBJECT_MAPPER.readTree(requestBody);
                JsonNode center = body.at("/locationRestriction/circle/center");
                double jitteredLatitude = center.get("latitude").asDouble();
                double jitteredLongitude = center.get("longitude").asDouble();

                assertEquals("restaurant", body.get("includedTypes").get(0).asText());
                assertEquals(20, body.get("maxResultCount").asInt());
                assertEquals("DISTANCE", body.get("rankPreference").asText());
                assertEquals(500.0, body.at("/locationRestriction/circle/radius").asDouble());
                assertTrue(distanceMeters(latitude, longitude, jitteredLatitude, jitteredLongitude) <= 500.000001);
            })
            .andRespond(withSuccess("""
                {
                  "places": [
                    {
                      "id": "google-place-1",
                      "displayName": {
                        "text": "구글식당",
                        "languageCode": "ko"
                      },
                      "formattedAddress": "서울특별시 중구 세종대로 1",
                      "location": {
                        "latitude": 37.5665,
                        "longitude": 126.9780
                      },
                      "primaryTypeDisplayName": {
                        "text": "음식점",
                        "languageCode": "ko"
                      },
                      "types": ["restaurant"],
                      "googleMapsUri": "https://maps.google.com/?cid=1"
                    }
                  ]
                }
                """, MediaType.APPLICATION_JSON));

        Optional<Restaurant> result = source.searchNear(String.valueOf(longitude), String.valueOf(latitude));

        assertTrue(result.isPresent());
        assertEquals("구글식당", result.get().placeName());
        assertEquals("google", result.get().source());
        assertEquals("https://maps.google.com/?cid=1", result.get().placeUrl());
        server.verify();
    }

    @Test
    @DisplayName("키가 있으면 실제 Google API를 호출하는 통합 테스트")
    void searchNear_integration_callRealGoogleApiWhenKeyPresent() {
        String apiKey = System.getenv("ANYMEAL_GOOGLE_API_KEY");
        Assumptions.assumeTrue(apiKey != null && !apiKey.isBlank(), "ANYMEAL_GOOGLE_API_KEY not set");

        GoogleRestaurantSearchSource source = new GoogleRestaurantSearchSource(apiKey);
        Optional<Restaurant> result = source.searchNear("126.983618", "37.572043");

        System.out.println("result: " + result);
        Assumptions.assumeTrue(result.isPresent(), "Google API returned no result or the key is not usable");
        assertEquals("google", result.get().source());
        assertFalse(result.get().placeName().isBlank());
    }

    private static double distanceMeters(
        double latitude1,
        double longitude1,
        double latitude2,
        double longitude2
    ) {
        double latitudeDistance = Math.toRadians(latitude2 - latitude1);
        double longitudeDistance = Math.toRadians(longitude2 - longitude1);
        double startLatitude = Math.toRadians(latitude1);
        double endLatitude = Math.toRadians(latitude2);

        double haversine = Math.sin(latitudeDistance / 2.0) * Math.sin(latitudeDistance / 2.0)
            + Math.cos(startLatitude) * Math.cos(endLatitude)
            * Math.sin(longitudeDistance / 2.0) * Math.sin(longitudeDistance / 2.0);

        return EARTH_RADIUS_METERS * 2.0 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1.0 - haversine));
    }
}
