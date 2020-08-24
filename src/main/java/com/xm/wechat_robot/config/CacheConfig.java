package com.xm.wechat_robot.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.locks.DefaultLockRegistry;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 */
@EnableCaching
@Configuration
public class CacheConfig {

    /**
     * Spring默认本地锁
     * @return
     */
    @Bean
    public DefaultLockRegistry getLock(){
        return new DefaultLockRegistry();
    }

    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        CaffeineCache clientStartCache = buildCache("wxApiService.startClient", ticker, 5);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(clientStartCache));
        return manager;
    }

    private CaffeineCache buildCache(String name, Ticker ticker, int secondsToExpire) {
        return new CaffeineCache(
                name,
                Caffeine.newBuilder()
                        .expireAfterWrite(secondsToExpire, TimeUnit.SECONDS)
                        .maximumSize(100)
                        .ticker(ticker)
                        .build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }
}
