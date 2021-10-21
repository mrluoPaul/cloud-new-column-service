package com.mrluo.cloud.common.utils.localmap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

@Slf4j
@RequiredArgsConstructor
public class LMapCacheFactory {
    private final Codec usingCodec;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TaskScheduler taskScheduler;

    public <K, V> LMapCache<K, V> getLocalMapCache(String name, LocalCachedMapOptions<K, V> options) {
        return getLocalMapCache(name, options, 0, 0);
    }

    public <K, V> LMapCache<K, V> getLocalMapCache(String name, LocalCachedMapOptions<K, V> options, long defaultTtl, long defaultIdle) {
        return new LMapCacheImpl<>(redissonClient, name, usingCodec, options,
                defaultTtl, defaultIdle).registerExpireCleanScheduler(taskScheduler::scheduleAtFixedRate);
    }
}
