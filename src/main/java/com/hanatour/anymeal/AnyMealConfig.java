package com.hanatour.anymeal;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("anymeal")
public class AnyMealConfig {
    private String defaultLongitude;
    private String defaultLatitude;
}
