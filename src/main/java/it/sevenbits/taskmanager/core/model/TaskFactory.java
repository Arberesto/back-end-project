package it.sevenbits.taskmanager.core.model;

public class TaskFactory {

    //Create new Task
    public Task getNewTask(final String newId, final String newText, final TaskStatus newStatus) {
        return new SimpleTask(newId, newText, newStatus);
    }

    //Update existing Task
    public Task getNewTask(final String newId, final String newText,
                           final TaskStatus newStatus, final String createdAt) {
        return new SimpleTask(newId, newText, newStatus, createdAt);
    }


    //Return existing Task
    public Task getNewTask(String newId, final String newText, final TaskStatus newStatus,
                           final String createdAt, final String changedAt) {
        return new SimpleTask(newId, newText, newStatus, createdAt,changedAt);
    }
}
