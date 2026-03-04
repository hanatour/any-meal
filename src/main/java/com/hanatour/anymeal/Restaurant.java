package com.hanatour.anymeal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Restaurant(
    @JsonProperty("address_name") String addressName,
    @JsonProperty("category_group_code") String categoryGroupCode,
    @JsonProperty("category_group_name") String categoryGroupName,
    @JsonProperty("category_name") String categoryName,
    @JsonProperty("distance") String distance,
    @JsonProperty("id") String id,
    @JsonProperty("phone") String phone,
    @JsonProperty("place_name") String placeName,
    @JsonProperty("place_url") String placeUrl,
    @JsonProperty("road_address_name") String roadAddressName,
    @JsonProperty("x") String x,
    @JsonProperty("y") String y,
    @JsonProperty("source") String source) {

    public Restaurant {
        if (source == null) {
            source = "kakao";
        }
    }

    /**
     * 기존 Restaurant에 소스 식별자를 붙인 새 인스턴스를 반환한다.
     */
    public static Restaurant withSource(Restaurant r, String source) {
        return new Restaurant(
            r.addressName(), r.categoryGroupCode(), r.categoryGroupName(),
            r.categoryName(), r.distance(), r.id(), r.phone(), r.placeName(),
            r.placeUrl(), r.roadAddressName(), r.x(), r.y(), source);
    }
}
