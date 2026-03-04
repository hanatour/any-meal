package com.hanatour.anymeal;

import java.util.Optional;

public interface RestaurantSearchSource {

    /**
     * 주어진 좌표 근처의 식당을 검색해 하나 반환한다.
     *
     * @param x 경도 (longitude)
     * @param y 위도 (latitude)
     * @return 식당 정보가 있으면 Optional에 담아 반환, 없으면 empty
     */
    Optional<Restaurant> searchNear(String x, String y);
}
