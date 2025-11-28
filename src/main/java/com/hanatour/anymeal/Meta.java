package com.hanatour.anymeal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Meta(
    @JsonProperty("same_name") Object sameName,
    @JsonProperty("pageable_count") Integer pageableCount,
    @JsonProperty("total_count") Integer totalCount,
    @JsonProperty("is_end") Boolean isEnd) {
}
