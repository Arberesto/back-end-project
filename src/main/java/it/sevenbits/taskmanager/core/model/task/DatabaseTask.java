package it.sevenbits.taskmanager.core.model.task;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.sql.Timestamp;

/**
 * Simple task in taskmanager
 */

public class DatabaseTask implements Task {

    private String id;
    private String text;
    private TaskStatus status;
    private String createdAt;
    private String updatedAt;
    @JsonIgnore
    private String owner;

    /**
     *Contructor for creating new Task(createdAt and updatedAt will be created automatically)
     * @param newId id of Task
     * @param newText text of Task
     * @param status TaskStatus of Task
     */

    DatabaseTask(final String newId, final String newText, final TaskStatus status, final String owner) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        this.createdAt = Timestamp.from(Instant.now()).toString();
        this.updatedAt = Timestamp.from(Instant.now()).toString();
        this.owner = owner;
    }

    /**
     *Constructor for creating existing Task(only updatedAt will be created automatically)
     * @param newId id of Task
     * @param newText text of Task
     * @param status TaskStatus of Task
     * @param createdAt Date of creating this Object
     * @param owner id of owner of task
     */

    DatabaseTask(final String newId, final String newText, final TaskStatus status,
                 final String createdAt, final String owner) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = Timestamp.from(Instant.now()).toString();
        this.owner = owner;
    }

    /**
     *Constructor for creating existing Task
     * @param newId id of Task
     * @param newText text of Task
     * @param status TaskStatus of Task
     * @param createdAt Date of creating this Object
     * @param updatedAt Date of last changing this Object
     * @param owner id of owner of task
     */

    DatabaseTask(final String newId, final String newText, final TaskStatus status,
                 final String createdAt, final String updatedAt, final String owner) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
    }

    public String getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     * Get date of creation
     * @return String with date
     */

    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get date of last change
     * @return String with date
     */

    public String getUpdatedAt() {
        return updatedAt;
    }


    /**
     * Get owner of task
     * @return String with uuid of User
     */

    public String getOwner() {
        return this.owner;
    }

    @Override
    public String toString() {
        return String.format("Task {\n id: %s\ntext: %s\nstatus %s\ncreatedAt %s\nupdatedAt %s\n", id, text,
                status.toString(), createdAt, updatedAt);
    }

    @Override
    public boolean equals(final Object o) {
        if (o.getClass().getSimpleName().equals("DatabaseTask")) {
            DatabaseTask task = ((DatabaseTask) o);
            return ((this.id.equals(task.id)) & (this.text.equals(task.text)) & (this.status.is(task.status))
                    & (this.createdAt.equals(task.createdAt)) & (this.updatedAt.equals(task.updatedAt)));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
