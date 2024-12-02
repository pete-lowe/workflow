package com.petelowe.workflow.controllers;

import com.petelowe.workflow.domain.Workflow;
import com.petelowe.workflow.domain.WorkflowId;
import com.petelowe.workflow.services.TaskService;
import com.petelowe.workflow.services.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

    private final TaskService taskService;
    private final WorkflowService workflowService;

    public WorkflowController(TaskService taskService, WorkflowService workflowService) {
        this.taskService = taskService;
        this.workflowService = workflowService;
    }

    @PostMapping("/create")
    ResponseEntity<WorkflowId> saveWorkflow(@RequestBody Workflow workflow) {
        workflow.tasks().forEach(taskService::saveTaskDefinition);
        return ok(workflowService.saveWorkflow(workflow));
    }

    @GetMapping("/{workflowId}")
    public ResponseEntity<Workflow> getWorkflow(@PathVariable WorkflowId workflowId) {
        Optional<Workflow> workflow = workflowService.getWorkflow(workflowId);
        return workflow.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{workflowId}")
    public ResponseEntity<Void> executeWorkflow(@PathVariable WorkflowId workflowId) {

        //TODO: doesnt yet handle nested and child tasks correctly - dependent task flow can be
        // executed via task controller.
        workflowService.executeWorkflow(workflowId);
        return ResponseEntity.ok().build();
    }
}
