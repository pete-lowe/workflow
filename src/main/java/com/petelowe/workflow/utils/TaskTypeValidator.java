package com.petelowe.workflow.utils;

import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.TaskType;
import com.petelowe.workflow.exceptions.IncorrectProcessorException;

public class TaskTypeValidator {

    public static void validateTaskType(TaskType expectedType, Task task, String processor) {
        if (!task.getTaskType().equals(expectedType)) {
            throw new IncorrectProcessorException(
                    task, processor);
        }
    }

}
