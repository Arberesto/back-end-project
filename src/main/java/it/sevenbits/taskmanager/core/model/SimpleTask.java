package it.sevenbits.taskmanager.core.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.Map;

/**
 * Simple task in taskmanager
 */

public class SimpleTask implements Task {

    private final String id;
    private String text;
    private TaskStatus status;
    private String createdAt;

    /**
     * Public constructor
     *
     * @param newId   id of new task
     * @param newText text of new task
     */

    @JsonCreator
    public SimpleTask(@JsonProperty("id") final String newId, @JsonProperty("text") final String newText) {
        this.text = newText;
        this.id = newId;
        this.status = TaskStatus.inbox;
        this.createdAt = "now";
    }

    public SimpleTask(String newId, final String newText, final TaskStatus status) {
        this.text = newText;
        this.id = newId;
        this.status = status;
        this.createdAt = "now";
    }

    public String getId() {
        return this.id;
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
        Iterator<Map.Entry<String, JsonNode>> entryIterator = fields.fields();
        for (JsonNode jsonNode : fields) {
            Map.Entry<String, JsonNode> entry = entryIterator.next();
            switch (entry.getKey()) {
                case "status":
                    TaskStatus status = TaskStatus.resolveString(entry.getValue().asText());
                    if (status != TaskStatus.empty) {
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
     * Set time and date of creation
     */

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get a clone of this object
     *
     * @return new SimpleTask object that equal to this
     */

    public Task clone() {
        Task clone = new SimpleTask(this.id, this.text);
        clone.setStatus(this.status);
        return clone;


    }
}
