package it.sevenbits.taskmanager.core.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple task in taskmanager
 */

public class DatabaseTask implements Task {

    private String id;
    private String text;
    private TaskStatus status;
    private String createdAt;
    private String changedAt;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    /**
     * Public constructor
     *
     * @param newId   id of new task
     * @param newText text of new task
     */

    @JsonCreator
    public DatabaseTask(@JsonProperty("id") final String newId, @JsonProperty("text") final String newText) {
        this.text = newText;
        this.id = newId;
        this.status = TaskStatus.inbox;
        this.createdAt = simpleDateFormat.format(new Date());
        this.changedAt = simpleDateFormat.format(new Date());
    }

    /**
     *Contructor for creating new Task(createdAt and changedAt will be created automatically)
     * @param newId id of Task
     * @param newText text of Task
     * @param status TaskStatus of Task
     */

    DatabaseTask(final String newId, final String newText, final TaskStatus status) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        this.createdAt = simpleDateFormat.format(new Date());
        this.changedAt = simpleDateFormat.format(new Date());
    }

    /**
     *Constructor for creating existing Task(only changedAt will be created automatically)
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
        this.changedAt = simpleDateFormat.format(new Date());
    }

    /**
     *Constructor for creating existing Task
     * @param newId id of Task
     * @param newText text of Task
     * @param status TaskStatus of Task
     * @param createdAt Date of creating this Object
     * @param changedAt Date of last changing this Object
     */

    DatabaseTask(final String newId, final String newText, final TaskStatus status,
                 final String createdAt, final String changedAt) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        this.createdAt = createdAt;
        this.changedAt = changedAt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String newId) {
        this.id = newId;
    }

    public String getText() {
        return this.text;
    }

    /**
     * Set new value of text
     *
     * @param text new value to set
     */

    private void setText(final String text) {
        this.text = text;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(final TaskStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Update this task
     *
     * @param fields ObjectNode that contains JSON object with fields to change
     * @return true if successful change, else false
     */

    public boolean update(final ObjectNode fields) {
        try {
            Iterator<Map.Entry<String, JsonNode>> entryIterator = fields.fields();
            for (JsonNode ignored : fields) {
                Map.Entry<String, JsonNode> entry = entryIterator.next();
                switch (entry.getKey()) {
                    case "status":
                        TaskStatus status = TaskStatus.resolveString(entry.getValue().asText());
                        if (!status.is(TaskStatus.empty)) {
                            setStatus(status);
                        }
                        break;

                    case "text":
                        if ("".equals(entry.getValue().asText()) || "".equals(entry.getValue().asText().trim())) {
                            return false;
                        }
                        setText(entry.getValue().asText());
                        break;
                    default:
                        return false;
                }
            }
            setChangedAt(simpleDateFormat.format(new Date()));
        } catch (Exception e) {
            return false;
        }
        return true;
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
     * Set date of creation
     * @param createdAt Date String
     */

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get date of last change
     * @return String with date
     */

    public String getChangedAt() {
        return changedAt;
    }

    /**
     * Set time and date of last change
     * @param changedAt new date of last change
     */

    public void setChangedAt(final String changedAt) {
        this.changedAt = changedAt;
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
        sb.append("changedAt: ");
        sb.append(changedAt);
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (o.getClass().getSimpleName().equals("DatabaseTask")) {
            DatabaseTask task = ((DatabaseTask) o);
            return ((this.id.equals(task.id)) & (this.text.equals(task.text)) & (this.status.is(task.status))
                    & (this.createdAt.equals(task.createdAt)) & (this.changedAt.equals(task.changedAt)));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
