package it.sevenbits.taskmanager.core.service;

import it.sevenbits.taskmanager.core.model.TaskFactory.Task;
import it.sevenbits.taskmanager.core.model.TaskFactory.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import it.sevenbits.taskmanager.web.model.PatchTaskRequest;
import org.springframework.stereotype.Service;

@Service
public class SimpleTaskService implements TaskService {

    private TaskFactory factory;

    public SimpleTaskService(final TaskFactory factory) {
        this.factory = factory;
    }

    public Task update(final Task task, final PatchTaskRequest request) {
        try {
            return factory.getNewTask(task.getId(),
                    notNullStringOrDefault(request.getText(), task.getText()),
                    TaskStatus.resolveString(
                            notNullStringOrDefault(request.getStatus(), task.getStatus().toString())),
                    task.getCreatedAt(), task.getChangedAt());
        } catch (Exception e) {
            return task;
        }
    }

    public String notNullStringOrDefault(final String nullableString, final String defaultString) {
        if (nullableString == null) {
            return defaultString;
        } else return nullableString;
    }
}
