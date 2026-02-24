package com.example.docflow_service.utils.aop.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Map;
import java.util.UUID;

@Aspect
@Slf4j
@Component
public class PerformanceLoggingAspect {


    @Around("@annotation(loggable)")
    public Object loggable(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        initTraceId();

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("[START] {}", loggable.startMessage());
        Object proceed = joinPoint.proceed();
        watch.stop();
        log.info("[END] {} выполнен за {} мс {}",
                joinPoint.getSignature(),
                watch.getTotalTimeMillis(),
                loggable.endMessage()
        );
        return proceed;
    }

    private void initTraceId() {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        MDC.setContextMap(contextMap);
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
        String traceId = UUID.randomUUID().toString();

        MDC.put("traceId", traceId);
    }
}