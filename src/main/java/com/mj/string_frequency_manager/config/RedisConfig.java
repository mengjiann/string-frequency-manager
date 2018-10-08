package com.mj.string_frequency_manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public LettuceConnectionFactory connectionFactory() {

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(
                appConfig.getRedisHost(),appConfig.getRedisPort());

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate() {
        final RedisTemplate<String, Integer> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory());
        template.setEnableTransactionSupport(true);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;

    }


}
