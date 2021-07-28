package com.hanatour.anymeal;

import com.hanatour.anymeal.geocalc.Coordinate;
import com.hanatour.anymeal.geocalc.EarthCalc;
import com.hanatour.anymeal.geocalc.Point;
import org.junit.jupiter.api.Test;

public class EarthCalcTest {
  @Test
  public void pointAtTest(){
    //Kew
    Coordinate lat = Coordinate.fromDegrees(37.572043);
    Coordinate lng = Coordinate.fromDegrees(126.983618);
    Point kew = Point.at(lat, lng);

    //Distance away point, bearing is 45deg
    Point otherPoint = EarthCalc.gcd.pointAt(kew, 0, 1000);
    System.out.println(otherPoint);

    //하나투어빌딩
    //https://www.google.com/maps/@37.572043,126.983618,17z

    //결과
    //https://www.google.com/maps/@37.581056372974224,126.98361800000005,17z
  }
}
