package com.mrluo.cloud.common.utils.localmap.listener;

public interface MapEntryRemovedListener<K, V> extends MapEntryListener<K, V> {
    @Override
    void onRemoved(K key, V value);

    @Override
    default MapEntryEvent.Type onType() {
        return MapEntryEvent.Type.REMOVED;
    }

}
