package it.sevenbits.taskmanager.web.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for GET Request in TaskController
 */

public class AddTaskRequest {
    @JsonProperty
    private String text;

    /**
     * Default constructor for deserialization
     */

    public AddTaskRequest(){}

    /**
     * Public constructor
     * @param text text for new Task
     */

    public AddTaskRequest(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
