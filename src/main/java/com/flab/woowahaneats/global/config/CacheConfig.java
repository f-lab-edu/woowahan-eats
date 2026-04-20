package com.flab.woowahaneats.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.domain.restaurant.user.controller.dto.RestaurantResponse;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter.TtlFunction;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final long BASE_TTL_MINUTES = 30;
    private static final long JITTER_RANGE_MINUTES = 5;

    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<List<RestaurantResponse>> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, RestaurantResponse.class));

        TtlFunction jitteredTtl = (key, value) -> {
            long jitter = ThreadLocalRandom.current().nextLong(-JITTER_RANGE_MINUTES, JITTER_RANGE_MINUTES + 1);
            return Duration.ofMinutes(BASE_TTL_MINUTES + jitter);
        };

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(jitteredTtl);

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .initialCacheNames(Set.of("nearbyRestaurantsByCategory"))
                .enableStatistics()
                .build();
    }
}