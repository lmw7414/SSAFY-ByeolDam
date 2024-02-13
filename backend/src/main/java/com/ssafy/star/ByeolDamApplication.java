package com.ssafy.star;

import com.ssafy.star.common.config.properties.AppProperties;
import com.ssafy.star.common.config.properties.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        CorsProperties.class,
        AppProperties.class
})
public class ByeolDamApplication {

    public static void main(String[] args) {
        SpringApplication.run(ByeolDamApplication.class, args);
    }

}
