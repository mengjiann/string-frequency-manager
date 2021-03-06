package com.mj.string_frequency_manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.io.IOException;

@Configuration
public class RedisServerConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public RedisServer redisServer() throws IOException {
        RedisServer redisEmbeddedServer = new RedisServer(this.appConfig.getRedisPort());
        redisEmbeddedServer.start();
        return redisEmbeddedServer;
    }

}
