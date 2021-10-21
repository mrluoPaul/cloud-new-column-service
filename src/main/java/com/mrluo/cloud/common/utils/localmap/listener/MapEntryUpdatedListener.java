package com.mrluo.cloud.common.utils.localmap.listener;

public interface MapEntryUpdatedListener<K, V> extends MapEntryListener<K, V> {
    @Override
    void onUpdated(K key, V value, V oldValue);

    @Override
    default MapEntryEvent.Type onType() {
        return MapEntryEvent.Type.UPDATED;
    }
}