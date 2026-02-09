package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;

import java.util.List;

/**
 * Service interface for Task management operations.
 * Defines all business logic operations related to tasks.
 */
public interface TaskService {
    
    /**
     * Create a new task
     * @param task the task to create
     * @return the created task
     */
    Task create(Task task);
    
    /**
     * Retrieve a task by id
     * @param id the task id
     * @return the task
     */
    Task get(String id);
    
    /**
     * Update an existing task
     * @param id the task id
     * @param task the task updates
     * @return the updated task
     */
    Task update(String id, Task task);
    
    /**
     * Delete a task
     * @param id the task id
     */
    void delete(String id);
    
    /**
     * Get all tasks sorted by due date
     * @return list of all tasks
     */
    List<Task> getAll();
    
    /**
     * Get all tasks filtered by status, sorted by due date
     * @param status the task status to filter by
     * @return list of filtered tasks
     */
    List<Task> getByStatus(Task.Status status);
}
