package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.core.model.Task;

import java.util.Collection;

public interface TaskRepository {

    Task createTask (String text);

    Collection<Task> getTaskList();

    Task getTask(String id);

    Task deleteTask(String id);

    Task updateTask(String id, Task changedTask);
}
