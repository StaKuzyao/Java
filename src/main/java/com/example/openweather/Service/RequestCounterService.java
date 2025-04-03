package com.example.openweather.Service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RequestCounterService {

    private final AtomicInteger counter = new AtomicInteger();


    public void increment() {
        counter.incrementAndGet();
    }


    public int getCount() {
        return counter.get();
    }
}
