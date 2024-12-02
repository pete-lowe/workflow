package com.petelowe.workflow.services;


import com.petelowe.workflow.converters.TaskConverter;
import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.TaskId;
import com.petelowe.workflow.domain.TaskType;
import com.petelowe.workflow.domain.daos.TaskDefinitionDao;
import com.petelowe.workflow.queues.TaskQueue;
import com.petelowe.workflow.repositories.TaskDefinitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TaskService {

    private final TaskDefinitionRepository taskDefinitionRepository;
    private final TaskQueue taskQueue;

    public TaskService(TaskDefinitionRepository taskDefinitionRepository, TaskQueue taskQueue) {
        this.taskDefinitionRepository = taskDefinitionRepository;
        this.taskQueue = taskQueue;
    }

    public TaskId saveTaskDefinition(Task task) {
        taskDefinitionRepository.saveTask(task);
        return new TaskId(task.getTaskId());
    }

    public Optional<Task> getTask(TaskId taskId) {
        return taskDefinitionRepository.getTaskById(taskId)
                .map(TaskConverter::toTask);
    }

    public List<Task> getTasksByType(TaskType taskType) {
        return taskDefinitionRepository.getTaskDefinitionsByType(taskType)
                .map(TaskConverter::toTask)
                .toList();
    }

    public void queueTask(Task task) {
        taskQueue.queueTask(task);
    }
}
