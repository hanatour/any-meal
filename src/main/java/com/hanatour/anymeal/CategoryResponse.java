package com.hanatour.anymeal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CategoryResponse {
  @JsonProperty("documents")
  private List<Restaurant> restaurants;
  private Meta meta;

  public List<Restaurant> getRestaurants() {
    return restaurants;
  }

  public void setRestaurants(List<Restaurant> restaurants) {
    this.restaurants = restaurants;
  }

  public Meta getMeta() {
    return meta;
  }

  public void setMeta(Meta meta) {
    this.meta = meta;
  }

  @Override
  public String toString() {
    return "CategoryResponse{" +
            "restaurants=" + restaurants +
            ", meta=" + meta +
            '}';
  }
}
