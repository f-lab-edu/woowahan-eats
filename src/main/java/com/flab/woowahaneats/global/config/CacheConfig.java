package com.flab.woowahaneats.global.config;

import ch.hsr.geohash.GeoHash;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.flab.woowahaneats.domain.restaurant.service.RestaurantCacheConstants;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter.TtlFunction;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final long BASE_TTL_MINUTES = 30;
    private static final long JITTER_RANGE_MINUTES = 5;
    private static final String CACHE_NAME = RestaurantCacheConstants.NEARBY_BY_CATEGORY;

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CACHE_NAME);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(2))
                .recordStats());
        return cacheManager;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build(),
                DefaultTyping.NON_FINAL);
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

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
                .initialCacheNames(Set.of(CACHE_NAME))
                .enableStatistics()
                .build();
    }

    @Bean
    @Primary
    public CacheManager twoLevelCacheManager(CaffeineCacheManager caffeineCacheManager,
                                              RedisCacheManager redisCacheManager) {
        return new TwoLevelCacheManager(caffeineCacheManager, redisCacheManager, Set.of(CACHE_NAME));
    }

    @Bean
    public KeyGenerator restaurantCacheKeyGenerator() {
        return (target, method, params) -> {
            Restaurant restaurant = (Restaurant) params[0];
            String geoHash = GeoHash.withCharacterPrecision(
                    restaurant.getLocation().latitude(),
                    restaurant.getLocation().longitude(),
                    RestaurantCacheConstants.GEOHASH_PRECISION
            ).toBase32();
            return RestaurantCacheConstants.buildKey(geoHash, restaurant.getCategory());
        };
    }
}