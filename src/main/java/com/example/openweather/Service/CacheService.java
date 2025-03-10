package com.example.openweather.Service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public Object getFromCache(String key) {
        return cache.get(key);
    }

    public void addToCache(String key, Object value) {
        cache.put(key, value);
    }

    public void clearCache() {
        cache.clear();
    }

    public Map<String, Object> getCache() {return cache;}
}
