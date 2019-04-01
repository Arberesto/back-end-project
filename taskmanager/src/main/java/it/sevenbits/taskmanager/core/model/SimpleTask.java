package it.sevenbits.taskmanager.core.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;


public class SimpleTask implements Task {

    private final String id;
    private String text;
    private TaskStatus status;
    private SimpleTask simpleTask;

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

    public void setText(String text) {
        this.text = text;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(final TaskStatus newStatus) {
        this.status = newStatus;
    }

    public boolean update(final ObjectNode fields) {
        Iterator<Map.Entry<String, JsonNode>> entryIterator = fields.fields();
        for (JsonNode jsonNode:  fields) {

            Map.Entry<String, JsonNode> entry = entryIterator.next();
            try {
                Field field = this.getClass().getDeclaredField(entry.getKey());
                switch (entry.getKey()) {
                    case "status":
                        setStatus(TaskStatus.done);
                        break;

                    case "text":
                        setText(entry.getValue().asText());
                        break;
                    default:
                        break;

                }
            } catch (NoSuchFieldException e) {
                System.out.println(String.format("There is no field - %s", entry.getKey()));
                return false;
            }
            System.out.println("Done");
        }
        return true;
    }

}
