package it.sevenbits.taskmanager.core.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple task in taskmanager
 */

public class SimpleTask implements Task {

    private final String id;
    private String text;
    private TaskStatus status;

    /**
     * Public constructor
     * @param newId id of new task
     * @param newText text of new task
     */

    @JsonCreator
    public SimpleTask(@JsonProperty("id") final String newId, @JsonProperty("text") final String newText) {
        this.text = newText;
        this.id = newId;
        this.status = TaskStatus.inbox;
    }

    public String getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    /**
     * Set new value of text
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
     * @param fields ObjectNode that contains JSON object with fields to change
     * @return true if successful change, else false
     */

    public boolean update(final ObjectNode fields) {
        Iterator<Map.Entry<String, JsonNode>> entryIterator = fields.fields();
        for (JsonNode jsonNode:  fields) {

            Map.Entry<String, JsonNode> entry = entryIterator.next();
            try {
                Field field = this.getClass().getDeclaredField(entry.getKey());
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
            } catch (NoSuchFieldException e) { //TODO add logger
                //System.out.println(String.format("There is no field - %s", entry.getKey()));
                return false;
            }
        }
        return true;
    }

    /**
     * Get a clone of this object
     * @return new SimpleTask object that equal to this
     */

    public Task clone() {
        Task clone = new SimpleTask(this.id, this.text);
        clone.setStatus(this.status);
        return clone;


    }
}
