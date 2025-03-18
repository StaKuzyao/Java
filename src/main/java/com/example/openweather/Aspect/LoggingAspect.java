package com.example.openweather.Aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.info(joinPoint.getSignature() + " выполнен за " + executionTime + "мс");
        return proceed;
    }

    @AfterThrowing(pointcut = "execution(* com.example.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        logger.error("Ошибка в " + joinPoint.getSignature() + ": " + ex.getMessage());
    }
}
