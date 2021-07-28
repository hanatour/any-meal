package com.hanatour.anymeal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Restaurant {
    /*
    {"address_name":"서울 종로구 공평동 5-1","category_group_code":"FD6","category_group_name":"음식점",
    "category_name":"음식점 \u003e 샤브샤브","distance":"16","id":"8010365","phone":"",
    "place_name":"예현토렴","place_url":"http://place.map.kakao.com/8010365","road_address_name":"서울 종로구 우정국로 26",
    "x":"126.983559086706","y":"37.571897592667"}
     */
  @JsonProperty("address_name")
  private String addressName;
  @JsonProperty("category_group_code")
  private String categoryGroupCode;
  @JsonProperty("category_group_name")
  private String categoryGroupName;
  @JsonProperty("category_name")
  private String categoryName;
  @JsonProperty("distance")
  private String distance;
  @JsonProperty("id")
  private String id;
  @JsonProperty("phone")
  private String phone;
  @JsonProperty("place_name")
  private String placeName;
  @JsonProperty("place_url")
  private String placeUrl;
  @JsonProperty("road_address_name")
  private String roadAddressName;
  @JsonProperty("x")
  private String x;
  @JsonProperty("y")
  private String y;

  public String getAddressName() {
    return addressName;
  }

  public void setAddressName(String addressName) {
    this.addressName = addressName;
  }

  public String getCategoryGroupCode() {
    return categoryGroupCode;
  }

  public void setCategoryGroupCode(String categoryGroupCode) {
    this.categoryGroupCode = categoryGroupCode;
  }

  public String getCategoryGroupName() {
    return categoryGroupName;
  }

  public void setCategoryGroupName(String categoryGroupName) {
    this.categoryGroupName = categoryGroupName;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getDistance() {
    return distance;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPlaceName() {
    return placeName;
  }

  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }

  public String getPlaceUrl() {
    return placeUrl;
  }

  public void setPlaceUrl(String placeUrl) {
    this.placeUrl = placeUrl;
  }

  public String getRoadAddressName() {
    return roadAddressName;
  }

  public void setRoadAddressName(String roadAddressName) {
    this.roadAddressName = roadAddressName;
  }

  public String getX() {
    return x;
  }

  public void setX(String x) {
    this.x = x;
  }

  public String getY() {
    return y;
  }

  public void setY(String y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Restaurant that = (Restaurant) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Restaurant{" +
            "addressName='" + addressName + '\'' +
            ", categoryGroupCode='" + categoryGroupCode + '\'' +
            ", categoryGroupName='" + categoryGroupName + '\'' +
            ", categoryName='" + categoryName + '\'' +
            ", distance='" + distance + '\'' +
            ", id='" + id + '\'' +
            ", phone='" + phone + '\'' +
            ", placeName='" + placeName + '\'' +
            ", placeUrl='" + placeUrl + '\'' +
            ", roadAddressName='" + roadAddressName + '\'' +
            ", x='" + x + '\'' +
            ", y='" + y + '\'' +
            '}';
  }
}
