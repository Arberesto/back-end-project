package it.sevenbits.taskmanager.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddTaskRequest {
    @JsonProperty
    private String text;

    public String getText() {
        return text;
    }
}
