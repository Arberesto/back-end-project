package it.sevenbits.taskmanager.core.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

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
     * Set id of task
     * @param newId id of task
     */

    void setId(String newId);

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
     * Set status of task
     * @param newStatus status to set
     */

    void setStatus(TaskStatus newStatus);

    /**
     * Get date of creation
     * @return String with date
     */

    String getCreatedAt();

    /**
     * Set date of creation
     * @param createdAt Date String
     */

    void setCreatedAt(String createdAt);

    /**
     * Get date of last change
     * @return String with date
     */

    String getChangedAt();

    /**
     * Set date of last change
     * @param changedAt Date String
     */

    void setChangedAt(String changedAt);

    /**
     * Update this task
     * @param fields ObjectNode that contains JSON object with fields to change
     * @return true if successful change, else false
     */

    boolean update(ObjectNode fields);

}
