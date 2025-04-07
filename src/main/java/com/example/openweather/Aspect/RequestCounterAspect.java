package com.example.openweather.Aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.openweather.Service.RequestCounterService;

@Aspect
@Component
public class RequestCounterAspect {

    private final RequestCounterService requestCounterService;

    @Autowired
    public RequestCounterAspect(RequestCounterService requestCounterService) {
        this.requestCounterService = requestCounterService;
    }

    @Before("execution(* com.example.openweather.Service.*.*(..)) && !execution(* com.example.openweather.Service.RequestCounterService.*(..))")
    public void incrementCounter() {
        try {
            // Увеличиваем глобальный счётчик
            requestCounterService.increment();
        } catch (StackOverflowError e) {
            // Логирование ошибки для диагностики
            System.err.println("Ошибка переполнения стека в аспекте: " + e.getMessage());
        }
    }
}
