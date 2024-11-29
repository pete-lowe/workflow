package com.petelowe.workflow.domain;

import java.util.List;

public class EmailTask extends Task {
    private final String destinationEmail;
    private final String sourceEmail;
    private final String subject;
    private final String bodyContent;


    public EmailTask(String taskId,
                     List<String> subTasks,
                     String destinationEmail,
                     String sourceEmail,
                     String subject,
                     String bodyContent) {
        super(TaskType.EMAIL, taskId, subTasks);
        this.destinationEmail = destinationEmail;
        this.sourceEmail = sourceEmail;
        this.subject = subject;
        this.bodyContent = bodyContent;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getDestinationEmail() {
        return destinationEmail;
    }

    public String getSourceEmail() {
        return sourceEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getBodyContent() {
        return bodyContent;
    }
}
