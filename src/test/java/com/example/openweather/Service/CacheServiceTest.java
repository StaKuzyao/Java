package com.example.openweather.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CacheServiceTest {

    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new CacheService(); // Инициализация CacheService перед каждым тестом
    }

    @Test
    void shouldAddAndRetrieveValueFromCache() {

        String key = "testKey";
        String value = "testValue";


        cacheService.addToCache(key, value);
        Object cachedValue = cacheService.getFromCache(key);


        assertEquals(value, cachedValue);
    }

    @Test
    void shouldReturnNullForNonexistentKey() {

        Object cachedValue = cacheService.getFromCache("nonexistentKey");


        assertNull(cachedValue);
    }

    @Test
    void shouldClearAllEntriesFromCache() {

        cacheService.addToCache("key1", "value1");
        cacheService.addToCache("key2", "value2");


        cacheService.clearCache();
        Map<String, Object> cache = cacheService.getCache();


        assertEquals(0, cache.size());
    }
}
