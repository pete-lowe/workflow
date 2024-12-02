package com.petelowe.workflow.exceptions;

import com.petelowe.workflow.domain.Task;

public class IncorrectProcessorException extends RuntimeException {
    public IncorrectProcessorException(Task task,
                                       String processorClass) {
        super(String.format(
                "Failure attempting to process task %s of type %s, attempted using %s - " +
                        "please check processor configuration",
                task.getTaskId(), task.getTaskType().name(), processorClass));
    }
}
