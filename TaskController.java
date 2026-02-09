package com.example.taskmanager.controller;

import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Task Management API.
 * Handles all HTTP requests related to task operations.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    /**
     * POST /tasks - Create a new task
     */
    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        Task created = service.create(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /tasks/{id} - Retrieve a task by id
     */
    @GetMapping("/{id}")
    public Task get(@PathVariable String id) {
        return service.get(id);
    }

    /**
     * PUT /tasks/{id} - Update a task
     */
    @PutMapping("/{id}")
    public Task update(@PathVariable String id, @RequestBody Task task) {
        return service.update(id, task);
    }

    /**
     * DELETE /tasks/{id} - Delete a task
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /tasks - List all tasks with optional status filter and pagination
     */
    @GetMapping
    public List<Task> list(
            @RequestParam(required = false) Task.Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Task> tasks;
        if (status != null) {
            tasks = service.getByStatus(status);
        } else {
            tasks = service.getAll();
        }

        // Apply pagination
        int start = page * size;
        int end = Math.min(start + size, tasks.size());
        
        if (start >= tasks.size()) {
            return List.of();
        }

        return tasks.subList(start, end);
    }
}
