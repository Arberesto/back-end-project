package it.sevenbits.taskmanager.core.repository.tasks;

import it.sevenbits.taskmanager.core.model.task.Task;

import java.util.List;

/**
 * Interface for repository of Task Objects
 */

public interface TaskRepository {

    /**
     * Create new Task
     * @param text text of new Task
     * @return Task Object if created or empty task
     */

    Task createTask(String text, String owner);

    /**
     * Get list of currens tasks with some status
     * @param status which status task need to be in list
     * @return list of current tasks with chosen status
     */

    List<Task> getTaskList(String status);

    /**
     * Get Task by its id
     * @param id id of task
     * @return Task object with chosen id or empty task if not found
     */

    Task getTask(String id);

    /**
     * Delete task
     * @param id id of deleted task
     * @return task that was deleted or empty task
     */

    Task deleteTask(String id);

    /**
     * Update task
     * @param id id of task to update
     * @param changedTask updated version of task
     * @return updated task if success or empty task
     */

    Task updateTask(String id, Task changedTask);
}
