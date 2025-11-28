package com.hanatour.anymeal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CategoryResponse(
    @JsonProperty("documents") List<Restaurant> restaurants,
    Meta meta) {
}
