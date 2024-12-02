package com.petelowe.workflow.exceptions;

import com.petelowe.workflow.domain.Task;

public class NoProcessorConfiguredException extends RuntimeException {
    public NoProcessorConfiguredException(Task task) {
        super(String.format(
                "There is no exception configured for task type %s", task.getTaskType()));
    }
}
