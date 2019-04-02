package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.core.model.Task;

import java.util.List;

public interface TaskRepository {

    Task createTask (String text);

    List<Task> getTaskList(String status);


    Task getTask(String id);

    Task deleteTask(String id);

    Task updateTask(String id, Task changedTask);
}
