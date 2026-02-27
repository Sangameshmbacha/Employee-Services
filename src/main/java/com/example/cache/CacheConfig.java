package com.example.cache;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {

    private final CacheProperties cacheProperties;

    public CacheConfig(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Bean("caffeineCacheManager")
    @Primary
    public CacheManager caffeineCacheManager() {

        CaffeineCacheManager cacheManager =
                new CaffeineCacheManager(
                        "designationCache",
                        "skillCache",
                        "departmentCache"
                );

        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(cacheProperties.getCaffeine().getMaximumSize())
                        .expireAfterWrite(
                                Duration.ofMinutes(
                                        cacheProperties.getCaffeine().getExpireAfterWriteMinutes()
                                )
                        )
        );

        return cacheManager;
    }

    @Bean("redisCacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration config =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(
                                Duration.ofMinutes(
                                        cacheProperties.getRedis().getTtlMinutes()
                                )
                        )
                        .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}