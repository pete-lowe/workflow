package com.petelowe.workflow.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class S3GetTask extends Task {
    private final String bucket;
    private final String fileName;

    public S3GetTask(List<Task> subTasks,
                     String bucket,
                     String fileName) {
        super(TaskType.S3_GET, subTasks, null);
        this.bucket = bucket;
        this.fileName = fileName;
    }
}
