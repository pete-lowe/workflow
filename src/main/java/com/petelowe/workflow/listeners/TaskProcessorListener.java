package com.petelowe.workflow.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.exceptions.NoProcessorConfiguredException;
import com.petelowe.workflow.processors.EmailTaskProcessor;
import com.petelowe.workflow.queues.TaskQueue;
import com.petelowe.workflow.processors.Processor;
import com.petelowe.workflow.processors.S3GetDataTaskProcessor;
import com.petelowe.workflow.processors.S3PutDataTaskProcessor;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
@Slf4j
public class TaskProcessorListener {

    private final ListableBeanFactory beanFactory;
    private final ObjectMapper objectMapper;
    private final TaskQueue taskQueue;

    public TaskProcessorListener(ListableBeanFactory beanFactory,
                                 ObjectMapper objectMapper,
                                 TaskQueue taskQueue) {
        this.beanFactory = beanFactory;
        this.objectMapper = objectMapper;
        this.taskQueue = taskQueue;
    }

    @SqsListener(queueNames = "${aws.task-queue}")
    public void receiveWithRetry(Message message)
            throws NoProcessorConfiguredException, JsonProcessingException {
        Task task = objectMapper.readValue(message.body(), Task.class);
        getTaskProcessor(task).execute(task);

        if (task.getSubTasks() != null) {
            task.getSubTasks()
                    .stream()
                    .peek(subTask -> subTask.setParentTaskId(task.getTaskId()))
                    .forEach(taskQueue::queueTask);
        }
    }

    private Processor getTaskProcessor(Task task) {
        return switch (task.getTaskType()) {
            case EMAIL -> beanFactory.getBean(EmailTaskProcessor.class);
            case S3_GET -> beanFactory.getBean(S3GetDataTaskProcessor.class);
            case S3_PUT -> beanFactory.getBean(S3PutDataTaskProcessor.class);
            default -> throw new NoProcessorConfiguredException(task);
        };
    }
}
