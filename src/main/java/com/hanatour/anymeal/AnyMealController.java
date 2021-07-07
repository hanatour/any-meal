package com.hanatour.anymeal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnyMealController {

  @GetMapping("restaurant/near")
  public String getRestaurantNear() {
    return AnyMealService.getRestaurantNear();
  }

}
