package com.example.taskmanager.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String id) {
        super("Task with id '" + id + "' not found");
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
