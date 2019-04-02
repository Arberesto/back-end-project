package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.core.model.SimpleTask;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Simple repository for tasks
 */

public class SimpleTaskRepository implements TaskRepository {

    private Map<String, Task> taskRepository;
    private final Task emptyTask;

    /**
     * public constructor, define empty task
     */

    public SimpleTaskRepository() {
        taskRepository = new HashMap<>();
        emptyTask = new SimpleTask("null", "emptyTask");
        emptyTask.setStatus(TaskStatus.empty);
    }

    /**
     * Create and return new task
     * @param text text for task
     * @return task, if it was created or empty task
     */

    public Task createTask(final String text) {
        String id = getNewId();
        Task task = new SimpleTask(id, text);
        taskRepository.put(id, task);
        return taskRepository.getOrDefault(id, emptyTask);
    }

    /**
     * Get list of task with some status
     * @param status which status task need to be in list
     * @return list of tasks with chosen status
     */

    public List<Task> getTaskList(final String status) {

        TaskStatus tasksToGet;

        if (status == null) {
            tasksToGet = TaskStatus.inbox;
        } else {
            tasksToGet = TaskStatus.resolveString(status);
            System.out.println("resolved status : " + tasksToGet);
        }

        if (TaskStatus.empty == tasksToGet) {
            return null;
        }
        List<Task> list = new ArrayList<>();
        Collection<Task> collection = taskRepository.values();
        for (Task item : collection) {
            System.out.println("Task status: " + item.getStatus());
            if (item.getStatus() == tasksToGet) {
                list.add(item);
                System.out.println("Task added");
            }
        }

        return list;
    }

    /**
     * Delete task by id
     * @param id id of Task
     * @return Deleted task if it was deleted or empty task
     */

    public Task deleteTask(final String id) {
        Task task = taskRepository.getOrDefault(id, emptyTask).clone();
        System.out.println("Deleted Task text: " + task.getText());
        System.out.println("Deleted Task id: " + task.getId());
        System.out.println("Deleted Task status: " + task.getStatus());
        taskRepository.remove(id);
        return task;
    }

    /**
     * Update task
     * @param id
     * @param changedTask new version of task
     * @return task after successful change or empty task
     */

    public Task updateTask(final String id, final Task changedTask) {
        taskRepository.remove(id);
        taskRepository.put(id, changedTask);
        return taskRepository.getOrDefault(id, emptyTask);
    }

    public String getNewId() {
        return UUID.randomUUID().toString();
    }

    public Task getTask(final String id) {
        return taskRepository.getOrDefault(id, emptyTask);
    }

}
