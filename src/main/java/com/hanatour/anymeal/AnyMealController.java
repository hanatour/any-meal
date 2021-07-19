package com.hanatour.anymeal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AnyMealController {

  @GetMapping("restaurant/near")
  public Optional<Restaurant> getRestaurantNear(@RequestParam(defaultValue="126.983618") String x, @RequestParam(defaultValue = "37.572043") String y) {
    System.out.println("x:" + x);
    System.out.println("y:" + y);
    return AnyMealService.getRestaurantNear(x, y);
  }

}
