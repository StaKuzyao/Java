package com.example.openweather.Service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RequestCounterService {

    // Глобальный потокобезопасный счётчик
    private final AtomicInteger counter = new AtomicInteger();

    // Метод для увеличения счётчика
    public void increment() {
        counter.incrementAndGet();
    }

    // Метод для получения текущего значения счётчика
    public int getCount() {
        return counter.get();
    }
}
