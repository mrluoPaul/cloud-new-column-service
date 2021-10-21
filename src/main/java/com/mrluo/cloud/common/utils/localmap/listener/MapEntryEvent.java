package com.mrluo.cloud.common.utils.localmap.listener;

import com.mrluo.cloud.common.utils.localmap.LMapCache;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MapEntryEvent<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Type {CREATED, UPDATED, REMOVED, EXPIRED}

    @With
    private transient LMapCache<K, V> source;

    private final Type type;
    private final K key;
    private final V value;
    private final V oldValue;
}
