package it.sevenbits.taskmanager.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.core.repository.PaginationInfo;

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
    /*
    {
  "_meta": {
    "total": 121,
    "page": 3,
    "size": 25,
    "next": "/tasks?status=done&order=desc&page=4&size=25",
    "prev": "/tasks?status=done&order=desc&page=2&size=25",
    "first": "/tasks?status=done&order=desc&page=1&size=25",
    "last": "/tasks?status=done&order=desc&page=5&size=25"
  },
  "tasks": [
    {
      "id": "d290f1ee-6c54-4b01-90e6-d701748f0851",
      "text": "Do practice",
      "status": "inbox",
      "createdAt": "2019-03-13T18:31:42+00:00",
      "updatedAt": "2019-03-14T19:33:01+00:00"
    }
  ]
}
     */

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
