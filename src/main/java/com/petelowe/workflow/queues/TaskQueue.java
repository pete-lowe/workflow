package com.petelowe.workflow.queues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.exceptions.UnableToQueueTaskException;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskQueue {

    private final String taskQueue;
    private final SqsTemplate template;
    private final ObjectMapper objectMapper;

    public TaskQueue(@Value("${aws.task-queue}") String taskQueue,
                     SqsTemplate template,
                     ObjectMapper objectMapper) {
        this.taskQueue = taskQueue;
        this.template = template;
        this.objectMapper = objectMapper;
    }

    public void queueTask(Task task) {
        try {
            template.send(taskQueue, objectMapper.writeValueAsString(task));
        } catch (Exception e) {
            throw new UnableToQueueTaskException(task, e);
        }
    }
}
