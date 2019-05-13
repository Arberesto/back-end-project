package it.sevenbits.taskmanager.core.model;

/**
 * Factory to create Task Objects
 */

public class TaskFactory {

    /**
     * Create clear new Task // (createdAt and changedAt field will be changed)
     * @param newId Id of task
     * @param newText Text of task
     * @param newStatus Status of task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText, final TaskStatus newStatus) {
        return new SimpleTask(newId, newText, newStatus);
    }

    /**
     * Update existing Task (changedAt field will be changed)
     * @param newId Id of task
     * @param newText Text of task
     * @param newStatus Status of task
     * @param createdAt Date of creating task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText,
                           final TaskStatus newStatus, final String createdAt) {
        return new SimpleTask(newId, newText, newStatus, createdAt);
    }


    /**
     * Get existing Task with no changes
     * @param newId Id of task
     * @param newText Text of task
     * @param newStatus Status of task
     * @param createdAt Date of creating task
     * @param changedAt Last date of creating task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText, final TaskStatus newStatus,
                           final String createdAt, final String changedAt) {
        return new SimpleTask(newId, newText, newStatus, createdAt, changedAt);
    }
}
