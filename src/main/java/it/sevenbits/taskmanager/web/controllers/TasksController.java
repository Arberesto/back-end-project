package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import it.sevenbits.taskmanager.core.service.TaskService;
import it.sevenbits.taskmanager.web.model.AddTaskRequest;
import it.sevenbits.taskmanager.web.model.PatchTaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Controller for creating tasks and get list of current tasks
 */

@Controller

public class TasksController {

    private TaskRepository taskRepository;
    private Logger logger;
    private TaskService taskService;

    /**
     * Public constructor
     *
     * @param taskRepository repository for tasks to use
     * @param taskService    service that work with Task Objects
     */

    public TasksController(final TaskRepository taskRepository, final TaskService taskService) {
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
    public ResponseEntity<Collection<Task>> getTaskList(final @RequestParam(name = "status", required = false)
                                                                String status,
                                                        final @RequestParam(name = "order", required = false)
                                                                String order,
                                                        final @RequestParam(name = "page", required = false)
                                                                String page,
                                                        final @RequestParam(name = "size", required = false)
                                                                Integer size) {
        List<Task> result;
        String statusToCreate;
        if (!TaskStatus.empty.is(status)) {
            statusToCreate = status;
        } else {
            statusToCreate = TaskStatus.inbox.toString();
        }
        result = taskRepository.getTaskList(statusToCreate);
        if (result != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(result);

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
     * <p>
     *
     * @param request Model that contains parameters for updating task
     * @return JSON with new Task Object; HttpStatus: CREATED if success or BAD_REQUEST if bad body
     */

    @RequestMapping(path = "/tasks", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> createTask(final @RequestBody AddTaskRequest request) {
        try {
            if (request == null) {
                logger.warn("text for creating task is null");
            } else {
                Task createdTask = taskRepository.createTask(request.getText());
                if (createdTask != null) {
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .location(URI.create(String.format("/tasks/%s", createdTask.getId())))
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(createdTask);

                }
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
     * status: OK if success, NOT_FOUND if there is no task with chosen id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> getTask(final @PathVariable String id) {
        if (isValideId(id)) {
            Task task = taskRepository.getTask(id);
            if (task != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(task);

            }
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
     * status: NO_CONTENT if success, BAD_REQUEST if bad body, NOT_FOUND if there is no current task with chosen id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.PATCH, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> patchTask(final @PathVariable String id, final @RequestBody PatchTaskRequest request) {
        if (isValideId(id)) {
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
     * status: OK if success, NOT_FOUND if no current task with that id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> deleteTask(final @PathVariable String id) {
        if (isValideId(id)) {
            Task result = taskRepository.deleteTask(id);
            if (result != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(result);
            }
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }

    /**
     * Check if id is actually UUID
     *
     * @param id UUID as String
     * @return true if valid, false if not
     */

    private boolean isValideId(final String id) {
        String pattern = "^[\\da-fA-F]{8}-[\\da-fA-F]{4}-[\\da-fA-F]{4}-[\\da-fA-F]{4}-[\\da-fA-F]{12}$";
        return Pattern.matches(pattern, id);

    }
}
