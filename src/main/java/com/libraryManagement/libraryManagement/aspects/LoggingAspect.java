package com.libraryManagement.libraryManagement.aspects;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class LoggingAspect {

	public static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    @Before("execution(* com.libraryManagement.libraryManagement.services..*(..))")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n===== METHOD CALL LOGGING =====")
                  .append("\nKind      : ").append(joinPoint.getKind())
                  .append("\nSignature : ").append(joinPoint.getSignature())
                  .append("\nArguments : ").append(StringUtils.join(joinPoint.getArgs(), ", "))
                  .append("\n================================\n");

        log.info(logMessage.toString());
    }

    @AfterThrowing(pointcut = "execution(* com.libraryManagement.libraryManagement.services..*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n===== EXCEPTION LOGGING =====")
                  .append("\nKind         : ").append(joinPoint.getKind())
                  .append("\nSignature    : ").append(joinPoint.getSignature())
                  .append("\nArguments    : ").append(StringUtils.join(joinPoint.getArgs(), ", "))
                  .append("\nException    : ").append(exception.getClass().getSimpleName())
                  .append("\nMessage      : ").append(exception.getMessage())
                  .append("\n================================\n");

        log.error(logMessage.toString());
    }
}
