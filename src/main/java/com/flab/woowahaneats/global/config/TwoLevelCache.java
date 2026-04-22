package com.flab.woowahaneats.global.config;

import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
public class TwoLevelCache implements Cache {

    private final String name;
    private final Cache localCache;
    private final Cache remoteCache;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    @Nullable
    public ValueWrapper get(Object key) {
        ValueWrapper value = localCache.get(key);
        if (value != null) {
            return value;
        }

        value = remoteCache.get(key);
        if (value != null) {
            localCache.put(key, value.get());
        }
        return value;
    }

    @Override
    @Nullable
    public <T> T get(Object key, @Nullable Class<T> type) {
        T value = localCache.get(key, type);
        if (value != null) {
            return value;
        }

        value = remoteCache.get(key, type);
        if (value != null) {
            localCache.put(key, value);
        }
        return value;
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        // L1 (Caffeine) 조회 — lock 없음
        ValueWrapper wrapper = localCache.get(key);
        if (wrapper != null) {
            return (T) wrapper.get();
        }

        // L2 (Redis) 조회 — lock 없음
        wrapper = remoteCache.get(key);
        if (wrapper != null) {
            T value = (T) wrapper.get();
            localCache.put(key, value);
            return value;
        }

        // 둘 다 miss → Caffeine lock으로 DB 조회만 수행 (네트워크 I/O 제외)
        T value = localCache.get(key, valueLoader);
        // Redis 저장은 lock 밖에서 수행
        remoteCache.put(key, value);
        return value;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        remoteCache.put(key, value);
        localCache.put(key, value);
    }

    @Override
    @Nullable
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        ValueWrapper existing = remoteCache.putIfAbsent(key, value);
        if (existing == null) {
            localCache.put(key, value);
        }
        return existing;
    }

    @Override
    public void evict(Object key) {
        remoteCache.evict(key);
        localCache.evict(key);
    }

    @Override
    public void clear() {
        remoteCache.clear();
        localCache.clear();
    }
}