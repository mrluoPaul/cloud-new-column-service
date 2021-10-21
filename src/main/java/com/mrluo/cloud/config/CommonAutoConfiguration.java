package com.mrluo.cloud.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.mrluo.cloud.common.defs.NewsDefs;
import com.mrluo.cloud.common.utils.localmap.LMapCacheFactory;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.LZ4Codec;
import org.redisson.codec.MarshallingCodec;
import org.redisson.codec.SnappyCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ClassUtils;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = NewsDefs.BASE_PACKAGE)
public class CommonAutoConfiguration {

    @Bean("customRedissonCodec")
    @ConditionalOnClass(MarshallingCodec.class)
    @ConditionalOnMissingBean(name = "customRedissonCodec")
    public Codec customRedissonCodec() {
        Codec redissonCodec = new MarshallingCodec();
        if (ClassUtils.isPresent("io.netty.handler.codec.compression.Snappy", null)) {
            redissonCodec = new SnappyCodec(redissonCodec);
        } else if (ClassUtils.isPresent("net.jpountz.lz4.LZ4Factory", null)) {
            redissonCodec = new LZ4Codec(redissonCodec);
        }
        return redissonCodec;
    }

    @Bean("mapCacheFactory")
    @ConditionalOnClass(RedissonClient.class)
    @ConditionalOnMissingBean(name = "mapCacheFactory")
    public LMapCacheFactory mapCacheFactory(@Autowired @Qualifier("customRedissonCodec") Codec customRedissonCodec) {
        return new LMapCacheFactory(customRedissonCodec);
    }

    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
