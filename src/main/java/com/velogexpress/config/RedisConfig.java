package com.velogexpress.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Wires the Redis-backed cache used across GET (read-through) and
 * write endpoints (POST/PUT/PATCH/DELETE evict the same cache region).
 *
 * Connection comes from spring.data.redis.url (REDIS_URL env var), which
 * accepts Railway's full redis://user:password@host:port form directly.
 *
 * A defensive CacheErrorHandler is registered so that if Redis is
 * unreachable, or a response type can't round-trip through JSON (e.g.
 * Spring Data's Page<T>, which has no default constructor), the request
 * falls back to hitting the database instead of failing with a 500.
 */
@Configuration
@EnableCaching
public class RedisConfig implements CachingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                log.warn("Redis cache GET failed (cache={}, key={}): {}", cache.getName(), key, exception.toString());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                log.warn("Redis cache PUT failed (cache={}, key={}): {}", cache.getName(), key, exception.toString());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                log.warn("Redis cache EVICT failed (cache={}, key={}): {}", cache.getName(), key, exception.toString());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.warn("Redis cache CLEAR failed (cache={}): {}", cache.getName(), exception.toString());
            }
        };
    }
}
