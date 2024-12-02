package com.petelowe.workflow.services;

import com.petelowe.workflow.converters.WorkflowConverter;
import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.TaskId;
import com.petelowe.workflow.domain.Workflow;
import com.petelowe.workflow.domain.WorkflowId;
import com.petelowe.workflow.domain.daos.WorkflowDao;
import com.petelowe.workflow.queues.TaskQueue;
import com.petelowe.workflow.repositories.TaskDefinitionRepository;
import com.petelowe.workflow.repositories.WorkflowRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.petelowe.workflow.converters.WorkflowConverter.toDao;
import static com.petelowe.workflow.converters.WorkflowConverter.toDto;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final TaskService taskService;
    private final TaskQueue taskQueue;

    public WorkflowService(WorkflowRepository workflowRepository, TaskService taskService, TaskQueue taskQueue) {
        this.workflowRepository = workflowRepository;
        this.taskService = taskService;
        this.taskQueue = taskQueue;
    }

    public WorkflowId saveWorkflow(Workflow workflow) {
        WorkflowDao dao = toDao(workflow);
        workflowRepository.saveWorkFlow(dao);
        return new WorkflowId(dao.getWorkflowId());
    }

    public Optional<Workflow> getWorkflow(WorkflowId workflowId) {
        return workflowRepository.getWorkflow(workflowId)
                .map(workflow -> {
                    List<Task> tasks =  Arrays.stream(workflow.getTaskIds()
                            .split(","))
                            .map(id -> taskService.getTask(new TaskId(id)))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList();
                    return toDto(workflow, tasks);
                });
    }

    public void executeWorkflow(WorkflowId workflowId) {
        getWorkflow(workflowId).ifPresent(wf -> wf.tasks().forEach(taskQueue::queueTask));
    }
}
