package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.core.model.SimpleTask;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleTaskRepository implements TaskRepository {

    private Map<String, Task> taskRepository;
    private final Task emptyTask;

    public SimpleTaskRepository() {
        taskRepository = new HashMap<>();
        emptyTask = new SimpleTask("null", "emptyTask");
        emptyTask.setStatus(TaskStatus.empty);
    }

    public Task createTask(final String text) {
        String id = getNewId();
        Task task = new SimpleTask(id, text);
        taskRepository.put(id, task);
        return taskRepository.getOrDefault(id, emptyTask);
    }

    public Collection<Task> getTaskList() {
        return taskRepository.values();
    }

    public Task deleteTask(final String id) {
        Task task = taskRepository.getOrDefault(id, emptyTask);
        taskRepository.remove(id);
        return task;
    }

    public Task updateTask(final String id, final Task changedTask) {
        //taskRepository.remove(id);
        //taskRepository.put(id, changedTask);
        //return taskRepository.getOrDefault(id, emptyTask);
        return changedTask;
    }

    public String getNewId() {
        return UUID.randomUUID().toString();
    }

    public Task getTask(final String id) {
        return taskRepository.getOrDefault(id, emptyTask);
    }

}
