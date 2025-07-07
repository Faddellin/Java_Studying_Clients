package org.example.clientsbackend.Application.Aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger("JSON_METHODS_LOGGER");

    @Around(value = "execution(public * org.example.clientsbackend..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.debug("Called >>>> {}.{}() --arguments: {}", className, methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        logger.debug("Returned <<<< {}.{}() --returned value: {}", className, methodName, result);
        return result;
    }
}
