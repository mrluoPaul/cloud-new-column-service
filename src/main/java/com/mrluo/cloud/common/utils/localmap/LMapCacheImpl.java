package com.mrluo.cloud.common.utils.localmap;

import com.mrluo.cloud.common.utils.localmap.listener.MapEntryEvent;
import com.mrluo.cloud.common.utils.localmap.listener.MapEntryListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.RedissonLocalCachedMap;
import org.redisson.RedissonObject;
import org.redisson.api.*;
import org.redisson.cache.CacheKey;
import org.redisson.cache.CacheValue;
import org.redisson.cache.LocalCacheView;
import org.redisson.client.codec.Codec;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class LMapCacheImpl<K, V> extends RedissonObject implements LMapCache<K, V> {
    private static final int CLEAN_BATCH_SIZE = 100;
    private static final long DEFAULT_CLEAN_PERIOD = 60 * 1000L;
    private static final String LISTENER_SUFFIX = "listener";
    private static final String LISTENER_TOPIC_SUFFIX = LISTENER_SUFFIX + ":topic";
    private static final String LISTENER_COUNTER_SUFFIX = LISTENER_SUFFIX + ":counter";

    private final RLocalCachedMap<K, V> entryMap;
    private final RLocalCachedMap<K, Long> ttlMap;
    private final RLocalCachedMap<K, Long> maxIdleMap;
    private final RLocalCachedMap<K, Long> usingIdleMap;

    private final RLocalCachedMap<MapEntryEvent.Type, Integer> listenerCounter;

    private final long defaultTtl;
    private final long defaultIdle;

    private final RedissonClient redissonClient;

    LMapCacheImpl(RedissonClient redissonClient, String name, Codec codec,
                  LocalCachedMapOptions<K, V> cachedMapOptions, long defaultTtl, long defaultIdle) {
        super(codec, ((Redisson) redissonClient).getCommandExecutor(), name);
        this.redissonClient = redissonClient;

        this.entryMap = redissonClient.getLocalCachedMap(name, codec, cachedMapOptions);

        this.ttlMap = redissonClient.getLocalCachedMap(suffixName(name, "ttl_map"), codec,
                copyOptions(cachedMapOptions));
        this.maxIdleMap = redissonClient.getLocalCachedMap(suffixName(name, "max_idle_map"), codec,
                copyOptions(cachedMapOptions));
        this.usingIdleMap = redissonClient.getLocalCachedMap(suffixName(name, "using_idle_map"), codec,
                copyOptions(cachedMapOptions));

        this.listenerCounter = redissonClient.getLocalCachedMap(suffixName(name, LISTENER_COUNTER_SUFFIX),
                LocalCachedMapOptions.<MapEntryEvent.Type, Integer>defaults()
                        .cacheSize(MapEntryEvent.Type.values().length + 1)
                        .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.NONE));

        ImproveLocalCacheView.improveCacheKey(this.listenerCounter);

        this.defaultTtl = defaultTtl;
        this.defaultIdle = defaultIdle;
    }

    LMapCacheImpl<K, V> registerExpireCleanScheduler(BiConsumer<Runnable, Duration> taskScheduler) {
        taskScheduler.accept(new ExpireCleaner(), Duration.ofMillis(DEFAULT_CLEAN_PERIOD));
        return this;
    }

    protected <K1, V1> LocalCachedMapOptions<K1, V1> copyOptions(LocalCachedMapOptions<K, V> copy) {
        return LocalCachedMapOptions.<K1, V1>defaults()
                .reconnectionStrategy(copy.getReconnectionStrategy())
                .syncStrategy(copy.getSyncStrategy())
                .evictionPolicy(copy.getEvictionPolicy())
                .cacheSize(copy.getCacheSize())
                .timeToLive(copy.getTimeToLiveInMillis())
                .cacheProvider(copy.getCacheProvider())
                .storeMode(copy.getStoreMode())
                .storeCacheMiss(true);
    }

    protected boolean isValid(K key) {
        long currentMillis = System.currentTimeMillis();
        return ttlMap.getOrDefault(key, Long.MAX_VALUE) > currentMillis
                && maxIdleMap.getOrDefault(key, Long.MAX_VALUE) > currentMillis;
    }

    protected void remainIdle(K key) {
        long usingRemainIdle = usingIdleMap.getOrDefault(key, -1L);
        if (usingRemainIdle < 0 && defaultIdle > 0) {
            usingRemainIdle = defaultIdle;
        }
        if (usingRemainIdle > 0) {
            maxIdleMap.fastPutAsync(key, System.currentTimeMillis() + usingRemainIdle);
        }
    }

    protected K convertKey(Object key) {
        return (K) key;
    }

    protected RTopic getListenerTopic() {
        return redissonClient.getTopic(suffixName(getRawName(), LISTENER_TOPIC_SUFFIX));
    }

    protected boolean hasListenerOn(MapEntryEvent.Type... onTypes) {
        for (MapEntryEvent.Type onType : onTypes) {
            if (listenerCounter.getOrDefault(onType, 0) > 0) {
                return true;
            }
        }
        return false;
    }

    protected void publishEntryEvent(MapEntryEvent event) {
        if (!hasListenerOn(event.getType())) {
            return;
        }
        getListenerTopic().publishAsync(event);
    }


    @Override
    public void addListener(MapEntryListener<K, V> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        MapEntryEvent.Type onType = listener.onType();
        if (onType == null) {
            for (MapEntryEvent.Type type : MapEntryEvent.Type.values()) {
                listenerCounter.addAndGetAsync(type, 1);
            }
        } else {
            listenerCounter.addAndGetAsync(onType, 1);
        }
        getListenerTopic().addListener(MapEntryEvent.class,
                ((channel, msg) -> listener.onMessage(channel, msg.withSource(this))));
    }

    @Override
    public int size() {
        return (int) entryMap.keySet().stream().filter(this::isValid).count();
    }

    @Override
    public boolean isEmpty() {
        return entryMap.keySet().stream().noneMatch(this::isValid);
    }

    @Override
    public boolean containsKey(Object key) {
        K usingKey = convertKey(key);
        if (!isValid(usingKey)) {
            return false;
        }

        remainIdle(usingKey);

        return entryMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return entrySet().stream().anyMatch(entry ->
                Objects.equals(entry.getValue(), value));
    }

    @Override
    public V get(Object key) {
        K usingKey = convertKey(key);
        if (!isValid(usingKey)) {
            return null;
        }

        remainIdle(usingKey);

        return entryMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return put(key, value, defaultTtl);
    }

    @Override
    public V put(K key, V value, long ttl) {
        return put(key, value, ttl, defaultIdle);
    }

    @Override
    public V put(K key, V value, long ttl, long maxIdle) {
        if (ttl > 0) {
            ttlMap.fastPutAsync(key, System.currentTimeMillis() + ttl);
        }

        if (maxIdle > 0) {
            if (maxIdle != defaultIdle) {
                usingIdleMap.fastPutAsync(key, maxIdle);
            }
            maxIdleMap.fastPutAsync(key, System.currentTimeMillis() + maxIdle);
        }

        if (!hasListenerOn(MapEntryEvent.Type.CREATED, MapEntryEvent.Type.UPDATED)) {
            return entryMap.put(key, value);
        }

        V oldValue = entryMap.put(key, value);

        if (!Objects.equals(oldValue, value)) {
            publishEntryEvent(MapEntryEvent.builder()
                    .key(key).value(value).type(MapEntryEvent.Type.CREATED).build());
        } else {
            publishEntryEvent(MapEntryEvent.builder()
                    .key(key).value(value).oldValue(oldValue)
                    .type(MapEntryEvent.Type.UPDATED).build());
        }

        return oldValue;
    }
    @Override
    public void fastPutIfAbsent(K key, V value, long ttl) {
        fastPut(key, value, ttl, defaultIdle);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        putAll(map, defaultTtl);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map, long ttl) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            fastPut(entry.getKey(), entry.getValue(), ttl);
        }
    }

    @Override
    public void fastPut(K key, V value) {
        fastPut(key, value, defaultTtl);
    }

    @Override
    public void fastPut(K key, V value, long ttl) {
        fastPut(key, value, ttl, defaultIdle);
    }

    @Override
    public void fastPut(K key, V value, long ttl, long maxIdle) {
        if (ttl > 0) {
            ttlMap.fastPutAsync(key, System.currentTimeMillis() + ttl);
        }

        if (maxIdle > 0) {
            if (maxIdle != defaultIdle) {
                usingIdleMap.fastPutAsync(key, maxIdle);
            }
            maxIdleMap.fastPutAsync(key, System.currentTimeMillis() + maxIdle);
        }

        if (!hasListenerOn(MapEntryEvent.Type.CREATED, MapEntryEvent.Type.UPDATED)) {
            entryMap.fastPutAsync(key, value);
            return;
        }

        V oldValue = entryMap.get(key);

        entryMap.fastPutAsync(key, value);

        if (!Objects.equals(oldValue, value)) {
            publishEntryEvent(MapEntryEvent.builder()
                    .key(key).value(value).type(MapEntryEvent.Type.CREATED).build());
        } else {
            publishEntryEvent(MapEntryEvent.builder()
                    .key(key).value(value).oldValue(oldValue)
                    .type(MapEntryEvent.Type.UPDATED).build());
        }
    }


    @Override
    public V remove(Object key) {
        K usingKey = convertKey(key);
        ttlMap.fastRemoveAsync(usingKey);
        maxIdleMap.fastRemoveAsync(usingKey);
        usingIdleMap.fastRemoveAsync(usingKey);

        V oldValue = entryMap.remove(key);

        if (!hasListenerOn(MapEntryEvent.Type.REMOVED)) {
            return oldValue;
        }

        publishEntryEvent(MapEntryEvent.builder()
                .key(key).value(oldValue)
                .type(MapEntryEvent.Type.REMOVED).build());

        return oldValue;
    }

    @Override
    public void fastRemove(K key) {
        ttlMap.fastRemoveAsync(key);
        maxIdleMap.fastRemoveAsync(key);
        usingIdleMap.fastRemoveAsync(key);

        if (!hasListenerOn(MapEntryEvent.Type.REMOVED)) {
            entryMap.fastRemoveAsync(key);
            return;
        }

        V oldValue = entryMap.get(key);

        entryMap.fastRemoveAsync(key);

        publishEntryEvent(MapEntryEvent.builder().key(key).value(oldValue)
                .type(MapEntryEvent.Type.REMOVED).build());
    }


    @Override
    public void clear() {
        ttlMap.clear();
        maxIdleMap.clear();
        entryMap.clear();
        usingIdleMap.clear();

        //TODO
    }

    @Override
    public Set<K> keySet() {
        return entryMap.keySet().stream().filter(this::isValid)
                .peek(this::remainIdle).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return entryMap.entrySet().stream().map(this::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entryMap.entrySet().stream()
                .peek(entry -> remainIdle(entry.getKey()))
                .collect(Collectors.toSet());
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return LMapCache.super.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        LMapCache.super.forEach(action);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putIfAbsent(key, value, defaultTtl);
    }

    @Override
    public V putIfAbsent(K key, V value, long ttl) {
        return putIfAbsent(key, value, ttl, defaultIdle);
    }

    @Override
    public V putIfAbsent(K key, V value, long ttl, long maxIdle) {
        if (isValid(key) && entryMap.containsKey(key)) {
            return null;
        }
        return put(key, value, ttl, maxIdle);
    }

    @Override
    public boolean remove(Object key, Object value) {
        if (entryMap.remove(key, value)) {
            K usingKey = convertKey(key);
            ttlMap.fastRemoveAsync(usingKey);
            maxIdleMap.fastRemoveAsync(usingKey);
            usingIdleMap.fastRemoveAsync(usingKey);
            publishEntryEvent(MapEntryEvent.builder()
                    .key(key).value(value).type(MapEntryEvent.Type.REMOVED).build());
            return true;
        }
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if (entryMap.replace(key, oldValue, newValue)) {
            remainIdle(key);
            publishEntryEvent(MapEntryEvent.builder().key(key).value(newValue)
                    .oldValue(oldValue).type(MapEntryEvent.Type.UPDATED).build());
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        if (isValid(key) || entryMap.containsKey(key)) {
            return put(key, value);
        }
        return null;
    }


    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        LMapCache.super.replaceAll(function);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        RLock lock = entryMap.getLock(key);
        lock.lock();
        try {
            V value = get(key);
            if (value == null) {
                V newValue = mappingFunction.apply(key);
                if (newValue != null) {
                    fastPut(key, newValue);
                    return newValue;
                }
                return null;
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        RLock lock = entryMap.getLock(key);
        lock.lock();
        try {
            V oldValue = get(key);
            if (oldValue == null) {
                return null;
            }

            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                fastPut(key, newValue);
                return newValue;
            }
            fastRemove(key);
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        RLock lock = entryMap.getLock(key);
        lock.lock();
        try {
            V oldValue = get(key);

            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue == null) {
                if (oldValue != null) {
                    fastRemove(key);
                }
            } else {
                fastPut(key, newValue);
            }
            return newValue;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        RLock lock = entryMap.getLock(key);
        lock.lock();
        try {
            V oldValue = get(key);
            V newValue = value;
            if (oldValue != null) {
                newValue = remappingFunction.apply(oldValue, value);
            }

            if (newValue == null) {
                fastRemove(key);
            } else {
                fastPut(key, newValue);
            }
            return newValue;
        } finally {
            lock.unlock();
        }
    }

    private class ExpireCleaner implements Runnable {
        @Override
        public void run() {
            log.debug("Do expire clean task for {}", getRawName());
            RLock expireCleanLock = redissonClient.getLock(suffixName(getRawName(), "expire_clean_lock"));
            if (!expireCleanLock.tryLock()) {
                return;
            }
            try {
                entryMap.keySet().stream().filter(key -> !isValid(key))
                        .limit(CLEAN_BATCH_SIZE)
                        .forEach(key -> {
                            log.debug("Clean expired key {}", key);
                            ttlMap.fastRemoveAsync(key);
                            maxIdleMap.fastRemoveAsync(key);
                            usingIdleMap.fastRemoveAsync(key);

                            if (!hasListenerOn(MapEntryEvent.Type.EXPIRED)) {
                                entryMap.fastRemoveAsync(key);
                            } else {
                                V value = entryMap.get(key);
                                entryMap.fastRemoveAsync(key);
                                publishEntryEvent(MapEntryEvent.builder().key(key).value(value)
                                        .type(MapEntryEvent.Type.EXPIRED).build());
                            }
                        });

                ttlMap.keySet().stream().filter(key -> !entryMap.containsKey(key))
                        .limit(CLEAN_BATCH_SIZE)
                        .forEach(ttlMap::fastRemoveAsync);

                maxIdleMap.keySet().stream().filter(key -> !entryMap.containsKey(key))
                        .limit(CLEAN_BATCH_SIZE)
                        .forEach(maxIdleMap::fastRemoveAsync);

                usingIdleMap.keySet().stream().filter(key -> !entryMap.containsKey(key))
                        .limit(CLEAN_BATCH_SIZE)
                        .forEach(usingIdleMap::fastRemoveAsync);

            } catch (Throwable e) {
                log.warn("Do expire cleaning exception {}", e.getMessage(), e);
            } finally {
                expireCleanLock.unlock();
            }
        }
    }

    private static class ImproveLocalCacheView<K, V> extends LocalCacheView<K, V> {

        @SneakyThrows
        static <K, V> void improveCacheKey(RLocalCachedMap<K, V> originMap) {
            Field cacheField = RedissonLocalCachedMap.class.getDeclaredField("cache");
            cacheField.setAccessible(true);
            Field localCacheViewField = RedissonLocalCachedMap.class.getDeclaredField("localCacheView");
            localCacheViewField.setAccessible(true);
            localCacheViewField.set(originMap, new ImproveLocalCacheView((Map<CacheKey, CacheValue>)
                    cacheField.get(originMap), (RedissonLocalCachedMap) originMap));
        }

        private final Map<Object, CacheKey> keyCaches = new IdentityHashMap<>();

        public ImproveLocalCacheView(Map<CacheKey, CacheValue> cache, RedissonObject object) {
            super(cache, object);
        }

        @Override
        public CacheKey toCacheKey(Object key) {
            return keyCaches.computeIfAbsent(key, usingKey ->
                    new ImproveCacheKey(key.toString().getBytes(StandardCharsets.UTF_8)));
        }
    }

    private static class ImproveCacheKey extends CacheKey {
        private final int hashCode;

        public ImproveCacheKey(byte[] keyHash) {
            super(keyHash);
            this.hashCode = super.hashCode();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

}
