package com.petelowe.workflow.notifications;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.petelowe.workflow.config.properties.AwsConfigurationProperties;
import com.petelowe.workflow.domain.Task;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Slf4j
@Component
public class NotificationAspect {

    private final AmazonSNSAsync snsClient;
    private final AwsConfigurationProperties awsConfigurationProperties;

    public NotificationAspect(AmazonSNSAsync snsClient, AwsConfigurationProperties awsConfigurationProperties) {
        this.snsClient = snsClient;
        this.awsConfigurationProperties = awsConfigurationProperties;
    }

    @Around("execution(* com.petelowe.workflow.processors.Processor.execute(..))")
    public Object sendTaskExecutionNotification(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant startTime = Instant.now();
        joinPoint.proceed();
        Instant endTime = Instant.now();
        Task task = (Task) joinPoint.getArgs()[0];
        snsClient.publish(
                "arn:aws:sns:us-east-1:000000000000:" + awsConfigurationProperties.taskExecutionNotificationTopic(),
                String.format("Execution of %s task %s with parent %s - started: %s finished: %s (%dms)",
                        task.getTaskType().name(),
                        task.getTaskId(),
                        task.getParentTaskId(),
                        startTime,
                        endTime,
                        Duration.between(startTime, endTime).toMillis())
                );
        return joinPoint;
    }



}
