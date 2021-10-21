package com.mrluo.cloud.config;


import com.mrluo.cloud.common.defs.NewsDefs;
import com.mrluo.cloud.common.utils.localmap.LMapCache;
import com.mrluo.cloud.common.utils.localmap.LMapCacheFactory;
import com.mrluo.cloud.common.utils.localmap.listener.MapEntryExpiredListener;
import com.mrluo.cloud.common.utils.localmap.listener.MapEntryRemovedListener;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Configuration
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@ConditionalOnClass(UsernamePasswordAuthenticationToken.class)
public class SessionAutoConfiguration {
    @Autowired
    private RedissonClient redissonClient;


    @Bean("userNameToTokenStore")
    @ConditionalOnMissingBean(name = "userNameToTokenStore")
    public RMapCache<String, String> userNameToTokenStore(
            @Autowired @Qualifier("customRedissonCodec") Codec customRedissonCodec) {
        return redissonClient.getMapCache(NewsDefs.PRODUCT_NAME + ".username_to_token", customRedissonCodec);
    }

    @Bean("tokenToAuthenticationStore")
    @ConditionalOnMissingBean(name = "tokenToAuthenticationStore")
    public LMapCache<String, UsernamePasswordAuthenticationToken> tokenToAuthenticationStore(
            @Autowired @Qualifier("mapCacheFactory") LMapCacheFactory mapCacheFactory,
            @Autowired @Qualifier("userNameToTokenStore") RMapCache<String, String> userNameToTokenStore) {
        LMapCache<String, UsernamePasswordAuthenticationToken> tokenToAuthenticationStore =
                mapCacheFactory.getLocalMapCache(NewsDefs.PRODUCT_NAME + ".token_to_auth", LocalCachedMapOptions
                        .<String, UsernamePasswordAuthenticationToken>defaults()
                        .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.SOFT));

        tokenToAuthenticationStore.addListener((MapEntryRemovedListener<String, UsernamePasswordAuthenticationToken>)
                (key, value) -> userNameToTokenStore.fastRemoveAsync(key));

        tokenToAuthenticationStore.addListener((MapEntryExpiredListener<String, UsernamePasswordAuthenticationToken>)
                (key, value) -> userNameToTokenStore.fastRemoveAsync(key));

        return tokenToAuthenticationStore;
    }
}