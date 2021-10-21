package com.mrluo.cloud.common.utils.localmap.listener;

import org.redisson.api.listener.MessageListener;

import java.util.Objects;

public interface MapEntryListener<K, V> extends MessageListener<MapEntryEvent<K, V>> {
    default void onMessage(CharSequence channel, MapEntryEvent<K, V> mapEntryEvent) {
        onMessage(mapEntryEvent);
    }

    default void onMessage(MapEntryEvent<K, V> mapEntryEvent) {
        MapEntryEvent.Type onType = onType();
        if (onType != null && !Objects.equals(onType(), mapEntryEvent.getType())) {
            return;
        }
        switch (mapEntryEvent.getType()) {
            case CREATED:
                onCreated(mapEntryEvent.getKey(), mapEntryEvent.getValue());
                return;
            case EXPIRED:
                onExpired(mapEntryEvent.getKey(), mapEntryEvent.getValue());
                return;
            case REMOVED:
                onRemoved(mapEntryEvent.getKey(), mapEntryEvent.getValue());
                return;
            case UPDATED:
                onUpdated(mapEntryEvent.getKey(), mapEntryEvent.getValue(), mapEntryEvent.getOldValue());
        }
    }

    MapEntryEvent.Type onType();

    default void onCreated(K key, V value) {

    }

    default void onUpdated(K key, V value, V oldValue) {

    }

    default void onRemoved(K key, V value) {

    }

    default void onExpired(K key, V value) {

    }
}