package org.example;

import org.ff4j.FF4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FF4JConfig {

    @Bean
    public FF4j ff4j() {
        FF4j ff4j = new FF4j();
        ff4j.createFeature("CHART_GENERATOR", true);

        return ff4j;
    }
}
