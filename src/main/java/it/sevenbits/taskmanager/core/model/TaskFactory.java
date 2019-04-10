package it.sevenbits.taskmanager.core.model;

public class TaskFactory {

    public Task getNewTask(final String newId, final String newText, final TaskStatus newStatus) {
        return new SimpleTask(newId, newText, newStatus);
    }

    public Task getNewTask(final String newId, final String newText) {
        return new SimpleTask(newId, newText);
    }
}
