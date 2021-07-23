package com.hanatour.anymeal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {
  /*
   "meta": {
    "same_name": null,
    "pageable_count": 11,
    "total_count": 11,
    "is_end": true
  }
  */

  @JsonProperty("same_name")
  private Object sameName;
  @JsonProperty("pageable_count")
  private Integer pageableCount;
  @JsonProperty("total_count")
  private Integer totalCount;
  @JsonProperty("is_end")
  private Boolean isEnd;

  public Object getSameName() {
    return sameName;
  }

  public void setSameName(Object sameName) {
    this.sameName = sameName;
  }

  public Integer getPageableCount() {
    return pageableCount;
  }

  public void setPageableCount(Integer pageableCount) {
    this.pageableCount = pageableCount;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Boolean getEnd() {
    return isEnd;
  }

  public void setEnd(Boolean end) {
    isEnd = end;
  }

  @Override
  public String toString() {
    return "Meta{" +
            "sameName=" + sameName +
            ", pageableCount=" + pageableCount +
            ", totalCount=" + totalCount +
            ", isEnd=" + isEnd +
            '}';
  }
}
