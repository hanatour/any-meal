package com.hanatour.anymeal;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    Logger locationLog = LoggerFactory.getLogger("location");

    public void logLocation(String x, String y, Optional<Restaurant> restaurant) {
        restaurant.ifPresent(r -> {
            locationLog.info("{}|{}|{}|{}|{}", x, y, r.id(), r.placeName(), r.addressName());
        });
    }
}
