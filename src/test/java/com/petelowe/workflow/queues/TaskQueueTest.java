package com.petelowe.workflow.queues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petelowe.workflow.domain.S3PutTask;
import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.exceptions.UnableToQueueTaskException;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskQueueTest {

    private static final String TASK_QUEUE = "test-task-queue";

    private TaskQueue taskQueue;
    private SqsTemplate sqsTemplate;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        sqsTemplate = mock(SqsTemplate.class);
        objectMapper = mock(ObjectMapper.class);

        taskQueue = new TaskQueue(TASK_QUEUE, sqsTemplate, objectMapper);
    }

    @Test
    void testQueueTask_Success() throws Exception {
        // Given
        Task task = mock(Task.class);
        String serializedTask = "serialized-task";
        when(objectMapper.writeValueAsString(task)).thenReturn(serializedTask);

        // When
        taskQueue.queueTask(task);

        // Then
        verify(sqsTemplate).send(TASK_QUEUE, serializedTask);
        verifyNoMoreInteractions(sqsTemplate);
    }

    @Test
    void testQueueTask_ThrowsUnableToQueueTaskException() throws Exception {
        // Given
        Task task = new S3PutTask(Collections.emptyList(), "bucket", "file");
        when(sqsTemplate.send(any(), any())).thenThrow(new RuntimeException());

        // When & Then
        UnableToQueueTaskException exception = assertThrows(
                UnableToQueueTaskException.class,
                () -> taskQueue.queueTask(task)
        );

        // Then
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("Serialization error", exception.getCause().getMessage());
    }
}
