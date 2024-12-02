package com.petelowe.workflow.config.aws;

import com.petelowe.workflow.config.properties.AwsConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DynamoDbConfig {

    private final AwsConfigurationProperties awsConfigProperties;

    public DynamoDbConfig(AwsConfigurationProperties awsConfigProperties) {
        this.awsConfigProperties = awsConfigProperties;
    }

    @Bean
    @ConditionalOnMissingBean({DynamoDbEnhancedClient.class})
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    @Bean
    public DynamoDbClient dynamoDbAsyncClientLocalStack() throws URISyntaxException {
        AwsCredentials cred = AwsBasicCredentials.create(
                awsConfigProperties.accessKey(),
                awsConfigProperties.secretKey());
        return DynamoDbClient
                .builder()
                .endpointOverride(new URI("http://" + getLocalStackHost() + ":4566"))
                .credentialsProvider(StaticCredentialsProvider.create(cred))
                .region(Region.US_EAST_1)
                .build();
    }

    public static String getLocalStackHost() {
        return System.getenv("DOCKER_HOST") != null ? "docker" :
                "localhost";
    }
}
