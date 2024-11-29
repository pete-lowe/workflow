package com.petelowe.workflow.processors;

import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.TaskType;
import com.petelowe.workflow.utils.TaskTypeValidator;
import org.springframework.stereotype.Component;

@Component
public class FetchDataTaskProcessor implements Processor {

    @Override
    public void execute(Task task) {
        TaskTypeValidator.validateTaskType(TaskType.FETCH, task, this.getClass().getName());
    }
}
