package it.sevenbits.taskmanager.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddTaskRequest {
    @JsonProperty
    private String text;

    public AddTaskRequest(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
