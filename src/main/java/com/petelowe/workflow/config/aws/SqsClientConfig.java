package com.petelowe.workflow.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.petelowe.workflow.config.properties.AwsConfigurationProperties;
import io.awspring.cloud.sqs.listener.QueueNotFoundStrategy;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;
import java.net.URISyntaxException;


@Configuration
public class SqsClientConfig {

    private final AwsConfigurationProperties awsConfigurationProperties;

    public SqsClientConfig(AwsConfigurationProperties awsConfigurationProperties) {
        this.awsConfigurationProperties = awsConfigurationProperties;
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(o -> o.queueNotFoundStrategy(QueueNotFoundStrategy.FAIL))
                .build();
    }

    @Bean
    public SqsAsyncClient amazonSqs() throws URISyntaxException {
        AwsBasicCredentials cred = AwsBasicCredentials.create(
                awsConfigurationProperties.accessKey(),
                awsConfigurationProperties.secretKey());
        return SqsAsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(cred))
                .endpointOverride(new URI("http://" + getLocalStackHost() + ":4566"))
                .build();
    }

    @Bean
    public AmazonSQSAsync amazonSQSAsync() throws URISyntaxException {
        BasicAWSCredentials cred = new BasicAWSCredentials(
                awsConfigurationProperties.accessKey(),
                awsConfigurationProperties.secretKey());
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                new URI("http://" + getLocalStackHost() + ":4566").toString(),
                                Region.US_EAST_1.toString()))
                .build();
    }

    private static String getLocalStackHost() {
        return System.getenv("DOCKER_HOST") != null ? "docker" :
                "localhost";
    }

}
