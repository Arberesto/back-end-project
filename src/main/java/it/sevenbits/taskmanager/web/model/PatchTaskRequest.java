package it.sevenbits.taskmanager.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatchTaskRequest {
    @JsonProperty
    private String text;
    private String status;

    public String getText() {
        return text;
    }

    public String getStatus() {
        return status;
    }
}
