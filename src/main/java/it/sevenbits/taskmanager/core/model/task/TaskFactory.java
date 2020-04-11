package it.sevenbits.taskmanager.core.model.task;

/**
 * Factory to create Task Objects
 */

public class TaskFactory {

    /**
     * Create clear new Task // (createdAt and changedAt field will be changed)
     *
     * @param newId     Id of task
     * @param newText   Text of task
     * @param newStatus Status of task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText, final TaskStatus newStatus) {
        return new DatabaseTask(newId, newText, newStatus);
    }

    /**
     * Update existing Task (changedAt field will be changed)
     *
     * @param newId     Id of task
     * @param newText   Text of task
     * @param newStatus Status of task
     * @param createdAt Date of creating task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText,
                           final TaskStatus newStatus, final String createdAt) {
        return new DatabaseTask(newId, newText, newStatus, createdAt);
    }


    /**
     * Get existing Task with no changes
     *
     * @param newId     Id of task
     * @param newText   Text of task
     * @param newStatus Status of task
     * @param createdAt Date of creating task
     * @param updatedAt Last date of creating task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText, final TaskStatus newStatus,
                           final String createdAt, final String updatedAt) {
        return new DatabaseTask(newId, newText, newStatus, createdAt, updatedAt);
    }
}
