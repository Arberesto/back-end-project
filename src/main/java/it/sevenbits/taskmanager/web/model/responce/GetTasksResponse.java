package it.sevenbits.taskmanager.web.model.responce;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.sevenbits.taskmanager.core.model.task.Task;
import it.sevenbits.taskmanager.core.repository.tasks.PaginationInfo;

import java.util.List;

/**
 * Model for GET response with pagination to /tasks
 */

public class GetTasksResponse {

    private String path = "/tasks";

    @JsonProperty
    private List<Task> tasks;

    @JsonProperty(value = "_meta")
    private PaginationInfo metaInfo;

    /**
     * Default constructor
     * @param tasks
     * @param page
     * @param size
     * @param status
     * @param totalSize
     */

    public GetTasksResponse(final List<Task> tasks, final String status, final String order, final Integer page, final Integer size,
                            final Integer totalSize) {
        this.tasks = tasks;
        this.metaInfo = new PaginationInfo(path, status, order, page, size, totalSize);
    }
}