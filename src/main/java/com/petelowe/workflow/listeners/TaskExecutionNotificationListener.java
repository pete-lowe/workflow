package com.petelowe.workflow.listeners;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
@Slf4j
public class TaskExecutionNotificationListener {

    //TODO: this is just to show the messages being processed, realistically this would be in another app or
    // some kind of AWS integration (e.g. processed by a lamda).

    @SqsListener(queueNames = "${aws.task-execution-notification-queue}")
    public void receiveWithRetry(Message message) {
        log.info(message.body());
    }

}
