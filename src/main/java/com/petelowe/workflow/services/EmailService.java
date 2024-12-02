package com.petelowe.workflow.services;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.petelowe.workflow.config.properties.AwsConfigurationProperties;
import com.petelowe.workflow.domain.EmailTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final AmazonSimpleEmailService simpleEmailService;
    private final AwsConfigurationProperties awsConfigurationProperties;

    public EmailService(AmazonSimpleEmailService simpleEmailService,
                        AwsConfigurationProperties awsConfigurationProperties) {
        this.simpleEmailService = simpleEmailService;
        this.awsConfigurationProperties = awsConfigurationProperties;
    }

    public void sendEmail(EmailTask emailTask) {
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(emailTask.getDestinationEmail()))
                .withMessage(new Message()
                        .withBody(new Body(new Content(emailTask.getBodyContent())))
                        .withSubject(new Content(emailTask.getSubject())))
                .withSource(awsConfigurationProperties.simpleEmailServiceSourceEmail());
        log.info("Sending email {} to {} for task {}",
                request.getMessage(), emailTask.getDestinationEmail(), emailTask.getTaskId());
        simpleEmailService.sendEmail(request);
    }

}
