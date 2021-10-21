package com.mrluo.cloud.common.utils.localmap.listener;

public interface MapEntryExpiredListener<K, V> extends MapEntryListener<K, V> {
    @Override
    void onExpired(K key, V value);

    @Override
    default MapEntryEvent.Type onType() {
        return MapEntryEvent.Type.EXPIRED;
    }
}
