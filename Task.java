package com.example.taskmanager.model;

import java.time.LocalDate;
import java.util.UUID;

public class Task {

    private String id;
    private String title;
    private String description;
    private Status status;
    private LocalDate dueDate;

    public enum Status {
        PENDING,
        IN_PROGRESS,
        DONE
    }

    public Task() {
        this.id = UUID.randomUUID().toString();
        this.status = Status.PENDING;
    }

    // getters and setters
}

//2.TaskRepository.java


package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TaskRepository {

    private final Map<String, Task> tasks = new HashMap<>();

    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public void delete(String id) {
        tasks.remove(id);
    }
}


//3.TaskService.java

package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;

import java.util.List;

public interface TaskService {
    Task create(Task task);
    Task get(String id);
    Task update(String id, Task task);
    void delete(String id);
    List<Task> getAll();
}

//4.TaskServiceImpl.java

package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("Task not found"));
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
        get(id);
        repository.delete(id);
    }

    @Override
    public List<Task> getAll() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .toList();
    }
}


//5.TaskController.java
package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(task));
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable String id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable String id,
                       @RequestBody Task task) {
        return service.update(id, task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Task> list() {
        return service.getAll();
    }
}

//6.Test

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServiceImpl service;

    @Test
    void newTask_shouldBePending() {
        Task task = new Task();
        task.setTitle("Test");
        task.setDueDate(LocalDate.now().plusDays(1));

        when(repository.save(any(Task.class))).thenReturn(task);

        Task saved = service.create(task);

        assertEquals(Task.Status.PENDING, saved.getStatus());
    }
}



