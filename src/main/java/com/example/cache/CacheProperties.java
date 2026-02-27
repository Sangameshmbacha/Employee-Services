package com.example.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    private final Caffeine caffeine = new Caffeine();
    private final Redis redis = new Redis();

    public Caffeine getCaffeine() {
        return caffeine;
    }

    public Redis getRedis() {
        return redis;
    }

    public static class Caffeine {
        private long maximumSize;
        private long expireAfterWriteMinutes;

        public long getMaximumSize() {
            return maximumSize;
        }

        public void setMaximumSize(long maximumSize) {
            this.maximumSize = maximumSize;
        }

        public long getExpireAfterWriteMinutes() {
            return expireAfterWriteMinutes;
        }

        public void setExpireAfterWriteMinutes(long expireAfterWriteMinutes) {
            this.expireAfterWriteMinutes = expireAfterWriteMinutes;
        }
    }

    public static class Redis {
        private long ttlMinutes;

        public long getTtlMinutes() {
            return ttlMinutes;
        }

        public void setTtlMinutes(long ttlMinutes) {
            this.ttlMinutes = ttlMinutes;
        }
    }
}