package com.petelowe.workflow.exceptions;

import com.petelowe.workflow.domain.TaskType;
public class IncorrectProcessorException extends RuntimeException {
    public IncorrectProcessorException(String taskId,
                                       TaskType taskType,
                                       String processorClass) {
        super(String.format(
                "Failure attempting to process task %s of type %s, attempted using %s - " +
                        "please check processor configuration", taskId, taskType.name(), processorClass));
    }
}
