package com.flab.woowahaneats.global.config;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
public class TwoLevelCacheManager implements CacheManager {

    private final CaffeineCacheManager caffeineCacheManager;
    private final RedisCacheManager redisCacheManager;
    private final Set<String> cacheNames;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    @Nullable
    public Cache getCache(String name) {
        if (!cacheNames.contains(name)) {
            return null;
        }
        return cacheMap.computeIfAbsent(name, this::createTwoLevelCache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Set.copyOf(cacheNames);
    }

    private Cache createTwoLevelCache(String cacheName) {
        Cache localCache = caffeineCacheManager.getCache(cacheName);
        Cache remoteCache = redisCacheManager.getCache(cacheName);
        if (localCache == null || remoteCache == null) {
            throw new IllegalStateException(
                    "Failed to initialize cache '%s': local or remote cache is null".formatted(cacheName));
        }
        return new TwoLevelCache(cacheName, localCache, remoteCache);
    }
}