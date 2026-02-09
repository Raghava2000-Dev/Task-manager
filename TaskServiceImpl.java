package com.example.taskmanager.service;

import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Implementation of TaskService with business logic for task management.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task create(Task task) {
        return repository.save(task);
    }

    @Override
    public Task get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public Task update(String id, Task updates) {
        Task existing = get(id);

        if (updates.getTitle() != null) {
            existing.setTitle(updates.getTitle());
        }
        if (updates.getDescription() != null) {
            existing.setDescription(updates.getDescription());
        }
        if (updates.getStatus() != null) {
            existing.setStatus(updates.getStatus());
        }
        if (updates.getDueDate() != null) {
            existing.setDueDate(updates.getDueDate());
        }

        return repository.save(existing);
    }

    @Override
    public void delete(String id) {
        get(id); // Verify task exists
        repository.delete(id);
    }

    @Override
    public List<Task> getAll() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .toList();
    }

    @Override
    public List<Task> getByStatus(Task.Status status) {
        return repository.findByStatus(status)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .toList();
    }
}

