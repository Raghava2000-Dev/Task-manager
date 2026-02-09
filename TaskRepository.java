package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final Map<String, Task> tasks = new HashMap<>();

    /**
     * Save a task (create or update)
     */
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    /**
     * Find a task by id
     */
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    /**
     * Find all tasks
     */
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * Find all tasks by status
     */
    public List<Task> findByStatus(Task.Status status) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Delete a task by id
     */
    public void delete(String id) {
        tasks.remove(id);
    }

    /**
     * Check if a task exists
     */
    public boolean existsById(String id) {
        return tasks.containsKey(id);
    }

    /**
     * Get total count of tasks
     */
    public long count() {
        return tasks.size();
    }

    /**
     * Clear all tasks (useful for testing)
     */
    public void deleteAll() {
        tasks.clear();
    }
}

