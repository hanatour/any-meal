package com.hanatour.anymeal.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverLocalResponse(
    @JsonProperty("items") List<NaverLocalItem> items
) {}
