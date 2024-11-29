package com.petelowe.workflow.domain;

import java.util.List;

public abstract class Task {

    protected final TaskType taskType;
    protected final String taskId;
    protected final List<String> subTasks;

    public Task(TaskType taskType, String taskId, List<String> subTasks) {
        this.taskType = taskType;
        this.taskId = taskId;
        this.subTasks = subTasks;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getTaskId() {
        return taskId;
    }

    public List<String> getSubTasks() {
        return subTasks;
    }
}

