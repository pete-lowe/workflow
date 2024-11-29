package com.petelowe.workflow.config;

import com.petelowe.workflow.config.properties.AwsConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleEmailServiceConfig {

    private final AwsConfigurationProperties awsConfigurationProperties;

    public SimpleEmailServiceConfig(AwsConfigurationProperties awsConfigurationProperties) {
        this.awsConfigurationProperties = awsConfigurationProperties;
    }
}
