package com.tekpyramid.sp.aop;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.tekpyramid.sp.service..*(..)) || " +
              "execution(* com.tekpyramid.sp.controller..*(..)) ")
    public void applicationPackagePointcut() {}

    @Around("applicationPackagePointcut()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        String args = Arrays.stream(joinPoint.getArgs())
                .map(arg -> {
                    if (arg == null) return "null";
                    return arg.getClass().getSimpleName() + "=" + arg.toString();
                })
                .collect(Collectors.joining(", "));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - startTime;
            log.info(" Executed {}.{}({}) in {} ms", className, methodName,"[..]", elapsed);
            return result;
        } catch (Throwable ex) {
            long elapsed = System.currentTimeMillis() - startTime;
            log.error(" Exception in {}.{}({}) after {} ms: {}", className, methodName, args, elapsed, ex.getMessage(), ex);
            throw ex;
        }
    }
}
