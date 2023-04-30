package com.limatech.juriprocessos.exceptions.users;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(String message) {
        super("Task not found " + message);
    }
}
