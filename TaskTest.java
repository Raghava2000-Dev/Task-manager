package com.example.taskmanager.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void constructor_noArgs_shouldInitializeWithDefaults() {
        Task task = new Task();

        assertNotNull(task.getId());
        assertEquals(Task.Status.PENDING, task.getStatus());
    }

    @Test
    void setTitle_shouldSet() {
        Task task = new Task();
        task.setTitle("Test Title");
        assertEquals("Test Title", task.getTitle());
    }

    @Test
    void setDescription_shouldSet() {
        Task task = new Task();
        task.setDescription("Test Description");
        assertEquals("Test Description", task.getDescription());
    }

    @Test
    void setStatus_shouldSet() {
        Task task = new Task();
        task.setStatus(Task.Status.IN_PROGRESS);
        assertEquals(Task.Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void setDueDate_shouldSet() {
        Task task = new Task();
        LocalDate dueDate = LocalDate.now().plusDays(1);
        task.setDueDate(dueDate);
        assertEquals(dueDate, task.getDueDate());
    }

    @Test
    void setId_shouldSet() {
        Task task = new Task();
        task.setId("123");
        assertEquals("123", task.getId());
    }

    @Test
    void newTask_shouldHavePendingStatus() {
        Task task = new Task();
        assertEquals(Task.Status.PENDING, task.getStatus());
    }

    @Test
    void newTask_shouldHaveUniqueId() {
        Task task1 = new Task();
        Task task2 = new Task();
        assertNotEquals(task1.getId(), task2.getId());
    }
}
