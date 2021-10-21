package com.mrluo.cloud.common.utils.localmap.listener;

public interface MapEntryCreatedListener<K, V> extends MapEntryListener<K, V> {
    @Override
    void onCreated(K key, V value);

    @Override
    default MapEntryEvent.Type onType() {
        return MapEntryEvent.Type.CREATED;
    }
}
