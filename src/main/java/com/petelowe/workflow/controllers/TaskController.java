package com.petelowe.workflow.controllers;

import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.TaskId;
import com.petelowe.workflow.domain.TaskType;
import com.petelowe.workflow.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskId> createTask(@RequestBody Task task) {
        return ok(taskService.saveTaskDefinition(task));
    }

    @GetMapping("/{taskType}")
    public ResponseEntity<List<Task>> getTasksByType(@PathVariable TaskType taskType) {
        return ok(taskService.getTasksByType(taskType));
    }

    @PostMapping
    public ResponseEntity<Void> queueTask(@RequestBody Task task) {
        taskService.queueTask(task);
        return ResponseEntity.ok().build();
    }

}
