package it.sevenbits.taskmanager.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for PATCH Request in TaskController
 */

public class PatchTaskRequest {
    @JsonProperty
    private String text;
    private String status;

    /**
     *
     * @param text new text for Task
     * @param status new status for Task
     */

    public PatchTaskRequest(final String text, final String status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public String getStatus() {
        return status;
    }
}
