package com.petelowe.workflow.listeners;

import org.springframework.stereotype.Component;

@Component
public class TaskListener {


    @Sqs
    public void receiveWithRetry() {

    }
}
