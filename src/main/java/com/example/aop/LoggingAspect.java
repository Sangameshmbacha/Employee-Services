package com.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceLayer() {}

    @Around("serviceLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        long startTime = System.currentTimeMillis();

        logger.info("Method execution started | Class: {} | Method: {}",
                className,
                methodName);

        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            logger.error("Exception occurred | Class: {} | Method: {} | Message: {}",
                    className,
                    methodName,
                    ex.getMessage());
            throw ex;
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("Method execution completed | Class: {} | Method: {} | Duration: {} ms",
                className,
                methodName,
                duration);

        return result;
    }
}