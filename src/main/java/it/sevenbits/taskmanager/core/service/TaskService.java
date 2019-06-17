package it.sevenbits.taskmanager.core.service;

import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.web.model.PatchTaskRequest;

public interface TaskService {

    public Task update(Task task, PatchTaskRequest request);
}
