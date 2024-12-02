package com.petelowe.workflow.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class SmsTask extends Task {
    private final String destinationNumber;
    private final String content;

    public SmsTask(List<Task> subTasks,
                   String destinationNumber,
                   String content) {
        super(TaskType.SMS, subTasks, null);
        this.destinationNumber = destinationNumber;
        this.content = content;
    }
}
