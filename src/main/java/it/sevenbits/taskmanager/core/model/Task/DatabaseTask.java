package it.sevenbits.taskmanager.core.model.Task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Simple task in taskmanager
 */

public class DatabaseTask implements Task {

    private String id;
    private String text;
    private TaskStatus status;
    private String createdAt;
    private String updatedAt;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    /**
     *Contructor for creating new Task(createdAt and updatedAt will be created automatically)
     * @param newId id of Task
     * @param newText text of Task
     * @param status TaskStatus of Task
     */

    DatabaseTask(final String newId, final String newText, final TaskStatus status) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        //this.createdAt = simpleDateFormat.format(new Date());
        //this.createdAt = Timestamp.from(Instant.now()).toLocaleString();
        this.createdAt = Timestamp.from(Instant.now()).toString();
        this.updatedAt = Timestamp.from(Instant.now()).toString();
        //this.updatedAt = simpleDateFormat.format(new Date());
    }

    /**
     *Constructor for creating existing Task(only updatedAt will be created automatically)
     * @param newId id of Task
     * @param newText text of Task
     * @param status TaskStatus of Task
     * @param createdAt Date of creating this Object
     */

    DatabaseTask(final String newId, final String newText, final TaskStatus status, final String createdAt) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = Timestamp.from(Instant.now()).toString();
        //this.updatedAt = simpleDateFormat.format(new Date());
    }

    /**
     *Constructor for creating existing Task
     * @param newId id of Task
     * @param newText text of Task
     * @param status TaskStatus of Task
     * @param createdAt Date of creating this Object
     * @param updatedAt Date of last changing this Object
     */

    DatabaseTask(final String newId, final String newText, final TaskStatus status,
                 final String createdAt, final String updatedAt) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
     *
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task {\n id: ");
        sb.append(id);
        sb.append("\n");
        sb.append("text: ");
        sb.append(text);
        sb.append("\n");
        sb.append("status: ");
        sb.append(status.toString());
        sb.append("\n");
        sb.append("createdAt: ");
        sb.append(createdAt);
        sb.append("\n}");
        sb.append("updatedAt: ");
        sb.append(updatedAt);
        sb.append("\n}");
        return sb.toString();
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
