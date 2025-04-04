package com.example.openweather.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CacheServiceTest {

    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new CacheService();
    }

    @ParameterizedTest
    @ValueSource(strings = {"testKey1", "testKey2", "testKey3"})
    void shouldAddAndRetrieveValueFromCache(String key) {

        String value = "testValue";


        cacheService.addToCache(key, value);
        Object cachedValue = cacheService.getFromCache(key);


        assertEquals(value, cachedValue);
    }

    @ParameterizedTest
    @ValueSource(strings = {"nonexistentKey1", "nonexistentKey2"})
    void shouldReturnNullForNonexistentKey(String key) {

        Object cachedValue = cacheService.getFromCache(key);


        assertNull(cachedValue);
    }

    @ParameterizedTest
    @ValueSource(strings = {"key1", "key2"})
    void shouldClearAllEntriesFromCache(String key) {

        cacheService.addToCache(key, "value1");
        cacheService.addToCache("anotherKey", "value2");


        cacheService.clearCache();
        Map<String, Object> cache = cacheService.getCache();


        assertEquals(0, cache.size());
    }
}
