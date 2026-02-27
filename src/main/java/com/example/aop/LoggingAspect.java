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

        long startTime = System.currentTimeMillis();

        logger.info("START: {}",
                joinPoint.getSignature().toShortString());

        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            logger.error("Exception in {} : {}",
                    joinPoint.getSignature().getName(),
                    ex.getMessage());
            throw ex;
        }

        long endTime = System.currentTimeMillis();

        logger.info("END: {} | Time Taken: {} ms",
                joinPoint.getSignature().getName(),
                (endTime - startTime));

        return result;
    }
}