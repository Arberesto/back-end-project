package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.core.model.Task.TaskStatus;
import it.sevenbits.taskmanager.core.repository.PaginationSort;
import it.sevenbits.taskmanager.core.repository.PaginationTaskRepository;
import it.sevenbits.taskmanager.core.service.TaskService;
import it.sevenbits.taskmanager.web.model.AddTaskRequest;
import it.sevenbits.taskmanager.web.model.PatchTaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * Controller for creating tasks and get list of current tasks
 */

@Controller

public class TasksController {

    private PaginationTaskRepository taskRepository;
    private Logger logger;
    private TaskService taskService;
    private final String idValidationPattern = "^[\\da-fA-F]{8}-[\\da-fA-F]{4}-[\\da-fA-F]{4}-[\\da-fA-F]{4}-[\\da-fA-F]{12}$";

    /**
     * Public constructor
     *
     * @param taskRepository repository for tasks to use
     * @param taskService    service that work with Task Objects
     */

    public TasksController(final PaginationTaskRepository taskRepository, final TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Get list of current tasks with some status
     *
     * @param status which status task need to be in list
     * @param order  Sorting order by creation date
     * @param page   Pagination page number
     * @param size   max number of elements on page
     * @return list of current tasks with chosen status
     */

    @RequestMapping(path = "/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Collection<Task>> getTaskList(
            @RequestParam(name = "status", required = false,
                    defaultValue = "inbox") final String status,
            @RequestParam(name = "order", required = false,
                    defaultValue = "desc") final String order,
            @Min(1) @RequestParam(name = "page", required = false,
                    defaultValue = "1") final Integer page,
            @Min(10) @Max(50) @RequestParam(name = "size", required = false,
                    defaultValue = "25") final Integer size) {
        try {
            List<Task> result;

            if (TaskStatus.resolveString(status) != null &&
                    PaginationSort.resolveString(order) != null) {

                String statusToGet = TaskStatus.resolveString(status).toString();
                String orderToGet = PaginationSort.resolveString(order).toString();
                result = taskRepository.getTaskList(statusToGet, orderToGet, page, size);
                if (result != null) {
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(result);

                } else {
                    logger.warn("result of getList is null!");
                }
            } else {
                logger.warn("status or order are invalid");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

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
    }

    /**
     * Create new task with inbox status
     *
     * @param request Model that contains parameters for updating task
     * @return JSON with new Task Object; HttpStatus: CREATED if success or BAD_REQUEST if bad body
     */

    @RequestMapping(path = "/tasks", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> createTask(@RequestBody final AddTaskRequest request) {
        try {
            Task createdTask = taskRepository.createTask(request.getText());
            if (createdTask != null) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create(String.format("/tasks/%s", createdTask.getId())))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(createdTask);

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Get current task
     *
     * @param id id of task to get
     * @return JSON with asked task (or with empty task if error within);
     * HttpStatus: OK if success, NOT_FOUND if there is no task with chosen id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> getTask(@Valid @Pattern(
            regexp = idValidationPattern) @PathVariable final String id) {
        Task task = taskRepository.getTask(id);
        if (task != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(task);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }

    /**
     * Patch some fields of task
     *
     * @param id      id of task to patch
     * @param request body Object that contain JSON with fields to update
     * @return JSON with updated task (or with empty task if some errors within);
     * HttpStatus: NO_CONTENT if success, BAD_REQUEST if bad body,
     * NOT_FOUND if there is no current task with chosen id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.PATCH, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> patchTask(@Valid @Pattern(
            regexp = idValidationPattern) @PathVariable final String id,
                                          @RequestBody final PatchTaskRequest request) {
        Task task = taskRepository.getTask(id);
        if (task != null) {
            Task result = taskRepository.updateTask(id, taskService.update(task, request));
            if (result != null) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(result);
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }

    /**
     * Delete task
     *
     * @param id id of task to delete
     * @return JSON with deleted object if deleted correctly (or with empty task if some errors within);
     * HttpStatus: OK if success, NOT_FOUND if no current task with that id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> deleteTask(@Valid @Pattern(
            regexp = idValidationPattern) @PathVariable final String id) {
        Task result = taskRepository.deleteTask(id);
        if (result != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(result);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }

}
