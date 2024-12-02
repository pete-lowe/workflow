package com.petelowe.workflow.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.petelowe.workflow.config.properties.AwsConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

import java.net.URI;
import java.net.URISyntaxException;


@Configuration
public class SnsClientConfig {

    private final AwsConfigurationProperties awsConfigurationProperties;

    public SnsClientConfig(AwsConfigurationProperties awsConfigurationProperties) {
        this.awsConfigurationProperties = awsConfigurationProperties;
    }

    @Bean
    public AmazonSNSAsync amazonSns() throws URISyntaxException {
        BasicAWSCredentials cred = new BasicAWSCredentials(
                awsConfigurationProperties.accessKey(),
                awsConfigurationProperties.secretKey());
        return AmazonSNSAsyncClientBuilder.standard()
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
