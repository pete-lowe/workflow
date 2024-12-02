package com.petelowe.workflow.domain;

import lombok.Getter;
import java.util.List;

@Getter
public class EmailTask extends Task {
    private final String destinationEmail;
    private final String subject;
    private final String bodyContent;

    public EmailTask(List<Task> subTasks,
                     String destinationEmail,
                     String subject,
                     String bodyContent) {
        super(TaskType.EMAIL, subTasks, null);
        this.destinationEmail = destinationEmail;
        this.subject = subject;
        this.bodyContent = bodyContent;
    }
}
