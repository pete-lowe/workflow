package com.petelowe.workflow.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "taskType",
        visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = EmailTask.class, name = "EMAIL"),
               @JsonSubTypes.Type(value = S3GetTask.class, name = "S3_GET"),
               @JsonSubTypes.Type(value = S3PutTask.class, name = "S3_PUT"),
               @JsonSubTypes.Type(value = SmsTask.class, name = "SMS")})
public abstract class Task {

    protected final TaskType taskType;
    protected final String taskId;
    protected final List<Task> subTasks;
    @Nullable
    protected String parentTaskId;

    public Task(TaskType taskType, List<Task> subTasks, @Nullable String parentTaskId) {
        this.taskType = taskType;
        this.taskId = UUID.randomUUID().toString();
        this.subTasks = subTasks;
        this.parentTaskId = parentTaskId;
    }

    public void setParentTaskId(@Nullable String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }
}

