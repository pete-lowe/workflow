package com.petelowe.workflow.exceptions;

import com.petelowe.workflow.domain.Task;

public class UnableToQueueTaskException extends RuntimeException {
    public UnableToQueueTaskException(Task task,
                                      Exception exception) {
        super(String.format(
                "Failure attempting to queue task %s of type %s - exception: %s",
                task.getTaskId(), task.getTaskType().name(), exception));
    }
}
