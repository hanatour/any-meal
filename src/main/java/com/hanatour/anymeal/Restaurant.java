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
    @JsonProperty("y") String y) {
}
