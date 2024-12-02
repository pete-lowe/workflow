package com.petelowe.workflow.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class S3PutTask extends Task {
    private final String bucket;
    private final String fileName;

    public S3PutTask(List<Task> subTasks,
                     String bucket,
                     String fileName) {
        super(TaskType.S3_PUT, subTasks, null);
        this.bucket = bucket;
        this.fileName = fileName;
    }
}
