package com.flab.woowahaneats.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.domain.restaurant.user.controller.dto.RestaurantResponse;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<List<RestaurantResponse>> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, RestaurantResponse.class));

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofMinutes(30));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .initialCacheNames(Set.of("nearbyRestaurantsByCategory"))
                .enableStatistics()
                .build();
    }
}