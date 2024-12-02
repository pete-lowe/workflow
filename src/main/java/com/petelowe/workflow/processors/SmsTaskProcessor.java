package com.petelowe.workflow.processors;

import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.TaskType;
import com.petelowe.workflow.utils.TaskTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsTaskProcessor implements Processor {

    @Override
    public void execute(Task task) {
        TaskTypeValidator.validateTaskType(TaskType.SMS, task, this.getClass().getName());
        log.info("Executing task: {} using {}", task.getTaskId(),  this.getClass().getName());
        try {
            Thread.sleep(3500);
        } catch (InterruptedException ignored) {

        }
    }
}
