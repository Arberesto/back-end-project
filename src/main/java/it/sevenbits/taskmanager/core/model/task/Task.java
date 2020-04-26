package it.sevenbits.taskmanager.core.model.task;

/**
 * Interface for task in taskmanager
 */

public interface Task {

    /**
     * Get id of task
     * @return id of task
     */

    String getId();

    /**
     * Get text of task
     * @return text
     */

    String getText();

    /**
     * Get status of task
     * @return status
     */

    TaskStatus getStatus();

    /**
     * Get date of creation
     * @return String with date
     */

    String getCreatedAt();

    /**
     * Get date of last change
     * @return String with date
     */

    String getUpdatedAt();

    /**
     * Get owner of task
     * @return String with uuid of User
     */

    String getOwner();
}
