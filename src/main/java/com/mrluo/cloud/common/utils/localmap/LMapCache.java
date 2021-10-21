package com.mrluo.cloud.common.utils.localmap;

import com.mrluo.cloud.common.utils.localmap.listener.MapEntryListener;
import org.redisson.api.RLock;

import java.util.concurrent.ConcurrentMap;

public interface LMapCache<K, V> extends ConcurrentMap<K, V> {
    void fastPut(K key, V value);

    void fastPut(K key, V value, long ttl);

    void fastPut(K key, V value, long ttl, long maxIdle);

    void fastRemove(K key);

    V putIfAbsent(K key, V value, long ttl);

    V putIfAbsent(K key, V value, long ttl, long maxIdle);

    V put(K key, V value, long ttl);

    V put(K key, V value, long ttl, long maxIdle);

    void fastPutIfAbsent(K key, V value);

    void fastPutIfAbsent(K key, V value, long ttl);

    void fastPutIfAbsent(K key, V value, long ttl, long maxIdle);

    void putAll(java.util.Map<? extends K, ? extends V> map, long ttl);

    void putAll(java.util.Map<? extends K, ? extends V> map, long ttl, long maxIdle);

    void updateEntryExpiration(K key, long ttl, long maxIdle);

    RLock getLock(K key);

    void addListener(MapEntryListener<K, V> mapEntryListener);

    void removeListener(MapEntryListener<K, V> mapEntryListener);
}
