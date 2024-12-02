package com.petelowe.workflow.services;

import com.petelowe.workflow.domain.*;
import com.petelowe.workflow.domain.daos.S3PutTaskDefinitionDao;
import com.petelowe.workflow.domain.daos.TaskDefinitionDao;
import com.petelowe.workflow.repositories.TaskDefinitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class TaskServiceTest {

    @Mock
    private TaskDefinitionRepository taskDefinitionRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void saveTaskDefinitionCallsCorrectRepoMethod() {
        //given, when
        Task emailTask = new EmailTask(null, "test@gmail.com", "subject", "body");
        taskService.saveTaskDefinition(emailTask);
        //then
        verify(taskDefinitionRepository).saveTask(emailTask);
    }

    @Test
    void getTaskDefinitionCallsCorrectRepoMethod() {
        //given, when
        TaskDefinitionDao testDao = new S3PutTaskDefinitionDao(
                "testPartition",
                "testSort",
                "taskId",
                TaskType.S3_PUT.name(),
                Instant.now(),
                DataType.TASK_DEFINITION,
                "bucket",
                "file");


        when(taskDefinitionRepository.getTaskById(any())).thenReturn(Optional.of(testDao));
        taskService.getTask(new TaskId("test"));
        //then
        verify(taskDefinitionRepository).getTaskById(eq(new TaskId("test")));
    }

    @Test
    void getTaskDefinitionByTypeCallsCorrectRepoMethod() {
        //given, when
        TaskDefinitionDao testDao = new S3PutTaskDefinitionDao(
                "testPartition",
                "testSort",
                "taskId",
                TaskType.S3_PUT.name(),
                Instant.now(),
                DataType.TASK_DEFINITION,
                "bucket",
                "file");


        when(taskDefinitionRepository.getTaskDefinitionsByType(TaskType.S3_PUT)).thenReturn(Stream.of(testDao));
        taskService.getTasksByType(TaskType.S3_PUT);
        //then
        verify(taskDefinitionRepository).getTaskDefinitionsByType(TaskType.S3_PUT);
    }

}
