package com.mj.string_frequency_manager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class AppConfig {

    @Value("${app.log.path}")
    private String appLogPath;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;


}
