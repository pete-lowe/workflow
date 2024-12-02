package com.petelowe.workflow.processors;

import com.petelowe.workflow.domain.EmailTask;
import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.TaskType;
import com.petelowe.workflow.services.EmailService;
import com.petelowe.workflow.utils.TaskTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailTaskProcessor implements Processor {

    private final EmailService emailService;

    public EmailTaskProcessor(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void execute(Task task) {
        TaskTypeValidator.validateTaskType(TaskType.EMAIL, task, this.getClass().getName());
        log.info("Executing task: {} using {}", task.getTaskId(),  this.getClass().getName());
        EmailTask emailTask = (EmailTask) task;
        emailService.sendEmail(emailTask);
    }
}
