package com.example.taskmanager.service;

import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServiceImpl service;

    private Task testTask;
    private LocalDate futureDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDate.now().plusDays(1);
        testTask = new Task();
        testTask.setTitle("Test Task");
        testTask.setDueDate(futureDate);
        testTask.setDescription("Test Description");
    }

    @Test
    void create_newTask_shouldBePending() {
        when(repository.save(any(Task.class))).thenReturn(testTask);

        Task saved = service.create(testTask);

        assertEquals(Task.Status.PENDING, saved.getStatus());
        verify(repository, times(1)).save(testTask);
    }

    @Test
    void create_newTask_shouldReturnSavedTask() {
        when(repository.save(any(Task.class))).thenReturn(testTask);

        Task saved = service.create(testTask);

        assertNotNull(saved);
        assertEquals(testTask.getTitle(), saved.getTitle());
    }

    @Test
    void get_validId_shouldReturnTask() {
        when(repository.findById("123")).thenReturn(Optional.of(testTask));

        Task result = service.get("123");

        assertNotNull(result);
        assertEquals(testTask.getTitle(), result.getTitle());
        verify(repository, times(1)).findById("123");
    }

    @Test
    void get_invalidId_shouldThrowTaskNotFoundException() {
        when(repository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            service.get("invalid");
        });
    }

    @Test
    void update_validId_shouldUpdateTitle() {
        testTask.setId("123");
        Task updates = new Task();
        updates.setTitle("Updated Title");

        when(repository.findById("123")).thenReturn(Optional.of(testTask));
        when(repository.save(any(Task.class))).thenReturn(testTask);

        Task result = service.update("123", updates);

        assertEquals("Updated Title", result.getTitle());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void update_invalidId_shouldThrowTaskNotFoundException() {
        Task updates = new Task();
        when(repository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            service.update("invalid", updates);
        });
    }

    @Test
    void update_statusChange_shouldUpdateStatus() {
        testTask.setId("123");
        Task updates = new Task();
        updates.setStatus(Task.Status.IN_PROGRESS);

        when(repository.findById("123")).thenReturn(Optional.of(testTask));
        when(repository.save(any(Task.class))).thenReturn(testTask);

        Task result = service.update("123", updates);

        assertEquals(Task.Status.IN_PROGRESS, result.getStatus());
    }

    @Test
    void delete_validId_shouldDeleteTask() {
        testTask.setId("123");
        when(repository.findById("123")).thenReturn(Optional.of(testTask));

        service.delete("123");

        verify(repository, times(1)).delete("123");
    }

    @Test
    void delete_invalidId_shouldThrowTaskNotFoundException() {
        when(repository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            service.delete("invalid");
        });

        verify(repository, never()).delete(any());
    }

    @Test
    void getAll_shouldReturnAllTasks() {
        List<Task> tasks = List.of(testTask);
        when(repository.findAll()).thenReturn(tasks);

        List<Task> result = service.getAll();

        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getByStatus_shouldReturnFilteredTasks() {
        Task pendingTask = new Task();
        pendingTask.setTitle("Pending");
        pendingTask.setDueDate(futureDate);
        pendingTask.setStatus(Task.Status.PENDING);

        when(repository.findByStatus(Task.Status.PENDING)).thenReturn(List.of(pendingTask));

        List<Task> result = service.getByStatus(Task.Status.PENDING);

        assertEquals(1, result.size());
        assertEquals(Task.Status.PENDING, result.get(0).getStatus());
        verify(repository, times(1)).findByStatus(Task.Status.PENDING);
    }
}
