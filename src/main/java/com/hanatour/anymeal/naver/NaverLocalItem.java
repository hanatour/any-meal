package com.hanatour.anymeal.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 네이버 지역 검색 API 응답의 개별 항목.
 * @see <a href="https://developers.naver.com/docs/serviceapi/search/local/local.md">지역 검색 API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverLocalItem(
    @JsonProperty("title") String title,
    @JsonProperty("link") String link,
    @JsonProperty("category") String category,
    @JsonProperty("description") String description,
    @JsonProperty("telephone") String telephone,
    @JsonProperty("address") String address,
    @JsonProperty("roadAddress") String roadAddress,
    @JsonProperty("mapx") Long mapx,
    @JsonProperty("mapy") Long mapy
) {}
