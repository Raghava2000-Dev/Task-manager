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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}



