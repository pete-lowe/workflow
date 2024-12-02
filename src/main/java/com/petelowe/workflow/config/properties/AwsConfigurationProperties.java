package com.petelowe.workflow.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record AwsConfigurationProperties(
        String accessKey,
        String secretKey,
        String simpleEmailServiceSourceEmail,
        String taskExecutionNotificationTopic,
        String taskExecutionNotificationQueue,
        String dynamoDbTableName
) {}
