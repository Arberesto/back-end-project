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
     * @param owner id of owner of task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText, final TaskStatus newStatus, final String owner) {
        return new DatabaseTask(newId, newText, newStatus, owner);
    }

    /**
     * Update existing Task (changedAt field will be changed)
     *
     * @param newId     Id of task
     * @param newText   Text of task
     * @param newStatus Status of task
     * @param createdAt Date of creating task
     * @param owner id of owner of task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText,
                           final TaskStatus newStatus, final String createdAt, final String owner) {
        return new DatabaseTask(newId, newText, newStatus, createdAt, owner);
    }


    /**
     * Get existing Task with no changes
     *
     * @param newId     Id of task
     * @param newText   Text of task
     * @param newStatus Status of task
     * @param createdAt Date of creating task
     * @param updatedAt Last date of creating task
     * @param owner id of owner of task
     * @return Task Object
     */
    public Task getNewTask(final String newId, final String newText, final TaskStatus newStatus,
                           final String createdAt, final String updatedAt, final String owner) {
        return new DatabaseTask(newId, newText, newStatus, createdAt, updatedAt, owner);
    }
}
