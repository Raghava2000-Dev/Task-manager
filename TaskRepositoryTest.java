package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {

    private TaskRepository repository;
    private Task testTask;

    @BeforeEach
    void setUp() {
        repository = new TaskRepository();
        testTask = new Task("Test Task", LocalDate.now().plusDays(1));
    }

    @Test
    void save_newTask_shouldStore() {
        Task saved = repository.save(testTask);

        assertNotNull(saved);
        assertEquals(testTask.getId(), saved.getId());
    }

    @Test
    void save_existingTask_shouldUpdate() {
        repository.save(testTask);
        testTask.setTitle("Updated Title");

        Task updated = repository.save(testTask);

        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    void findById_existingId_shouldReturn() {
        repository.save(testTask);

        Optional<Task> found = repository.findById(testTask.getId());

        assertTrue(found.isPresent());
        assertEquals(testTask.getId(), found.get().getId());
    }

    @Test
    void findById_nonExistingId_shouldReturnEmpty() {
        Optional<Task> found = repository.findById("non-existing");

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_noTasks_shouldReturnEmpty() {
        List<Task> all = repository.findAll();

        assertTrue(all.isEmpty());
    }

    @Test
    void findAll_multipleTasks_shouldReturnAll() {
        Task task1 = new Task("Task 1", LocalDate.now().plusDays(1));
        Task task2 = new Task("Task 2", LocalDate.now().plusDays(2));
        repository.save(task1);
        repository.save(task2);

        List<Task> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void delete_existingTask_shouldRemove() {
        repository.save(testTask);

        repository.delete(testTask.getId());

        assertTrue(repository.findById(testTask.getId()).isEmpty());
    }

    @Test
    void delete_nonExistingId_shouldDoNothing() {
        repository.delete("non-existing");

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void existsById_existingId_shouldReturnTrue() {
        repository.save(testTask);

        assertTrue(repository.existsById(testTask.getId()));
    }

    @Test
    void existsById_nonExistingId_shouldReturnFalse() {
        assertFalse(repository.existsById("non-existing"));
    }

    @Test
    void findByStatus_shouldReturnFiltered() {
        Task pendingTask = new Task("Pending", LocalDate.now().plusDays(1));
        pendingTask.setStatus(Task.Status.PENDING);

        Task doneTask = new Task("Done", LocalDate.now().plusDays(1));
        doneTask.setStatus(Task.Status.DONE);

        repository.save(pendingTask);
        repository.save(doneTask);

        List<Task> pending = repository.findByStatus(Task.Status.PENDING);

        assertEquals(1, pending.size());
        assertEquals(Task.Status.PENDING, pending.get(0).getStatus());
    }

    @Test
    void count_shouldReturnSize() {
        repository.save(testTask);
        repository.save(new Task("Task 2", LocalDate.now().plusDays(1)));

        assertEquals(2, repository.count());
    }

    @Test
    void deleteAll_shouldRemoveAllTasks() {
        repository.save(testTask);
        repository.save(new Task("Task 2", LocalDate.now().plusDays(1)));

        repository.deleteAll();

        assertEquals(0, repository.count());
    }
}
