package com.example.taskmanager.controller;

import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService service;

    @InjectMocks
    private TaskController controller;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setTitle("Test Task");
        testTask.setDueDate(LocalDate.now().plusDays(1));
    }

    // ===== CREATE TESTS =====
    @Test
    void create_validTask_shouldReturn201() {
        when(service.create(any(Task.class))).thenReturn(testTask);

        ResponseEntity<Task> response = controller.create(testTask);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testTask.getTitle(), response.getBody().getTitle());
    }

    // ===== GET TESTS =====
    @Test
    void get_validId_shouldReturnTask() {
        testTask.setId("123");
        when(service.get("123")).thenReturn(testTask);

        Task result = controller.get("123");

        assertNotNull(result);
        assertEquals(testTask.getTitle(), result.getTitle());
    }

    @Test
    void get_invalidId_shouldThrowTaskNotFoundException() {
        when(service.get("invalid")).thenThrow(new TaskNotFoundException("invalid"));

        assertThrows(TaskNotFoundException.class, () -> {
            controller.get("invalid");
        });
    }

    // ===== UPDATE TESTS =====
    @Test
    void update_validIdAndTask_shouldReturnTask() {
        testTask.setId("123");
        Task updates = new Task();
        updates.setTitle("Updated Title");

        when(service.update("123", updates)).thenReturn(testTask);

        Task result = controller.update("123", updates);

        assertNotNull(result);
        assertEquals(testTask.getTitle(), result.getTitle());
    }

    @Test
    void update_invalidId_shouldThrowTaskNotFoundException() {
        Task updates = new Task();
        when(service.update("invalid", updates)).thenThrow(new TaskNotFoundException("invalid"));

        assertThrows(TaskNotFoundException.class, () -> {
            controller.update("invalid", updates);
        });
    }

    // ===== DELETE TESTS =====
    @Test
    void delete_validId_shouldReturn204() {
        testTask.setId("123");
        doNothing().when(service).delete("123");

        ResponseEntity<Void> response = controller.delete("123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).delete("123");
    }

    @Test
    void delete_invalidId_shouldThrowTaskNotFoundException() {
        doThrow(new TaskNotFoundException("invalid")).when(service).delete("invalid");

        assertThrows(TaskNotFoundException.class, () -> {
            controller.delete("invalid");
        });
    }

    // ===== LIST TESTS =====
    @Test
    void list_noFilter_shouldReturnAllTasks() {
        List<Task> tasks = List.of(testTask);
        when(service.getAll()).thenReturn(tasks);

        List<Task> result = controller.list(null, 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void list_withStatusFilter_shouldReturnFilteredTasks() {
        testTask.setStatus(Task.Status.PENDING);
        List<Task> tasks = List.of(testTask);
        when(service.getByStatus(Task.Status.PENDING)).thenReturn(tasks);

        List<Task> result = controller.list(Task.Status.PENDING, 0, 10);

        assertEquals(1, result.size());
        assertEquals(Task.Status.PENDING, result.get(0).getStatus());
    }

    @Test
    void list_withPagination_shouldApplyPageSize() {
        List<Task> tasks = new java.util.ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Task t = new Task();
            t.setTitle("Task " + i);
            t.setDueDate(LocalDate.now().plusDays(1));
            tasks.add(t);
        }
        when(service.getAll()).thenReturn(tasks);

        List<Task> result = controller.list(null, 0, 5);

        assertEquals(5, result.size());
    }

    @Test
    void list_secondPage_shouldReturnSecondPageItems() {
        List<Task> tasks = new java.util.ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Task t = new Task();
            t.setTitle("Task " + i);
            t.setDueDate(LocalDate.now().plusDays(1));
            tasks.add(t);
        }
        when(service.getAll()).thenReturn(tasks);

        List<Task> result = controller.list(null, 1, 5);

        assertEquals(5, result.size());
    }

    @Test
    void list_beyondLastPage_shouldReturnEmpty() {
        List<Task> tasks = List.of(testTask);
        when(service.getAll()).thenReturn(tasks);

        List<Task> result = controller.list(null, 10, 10);

        assertTrue(result.isEmpty());
    }
}
