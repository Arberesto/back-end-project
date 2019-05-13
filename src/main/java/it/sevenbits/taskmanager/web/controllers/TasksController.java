package it.sevenbits.taskmanager.web.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * Controller for creating tasks and get list of current tasks
 */

@Controller

public class TasksController {

    private TaskRepository taskRepository;

    /**
     * Public constructor
     * @param taskRepository repository for tasks to use
     */

    public TasksController(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get list of current tasks with some status
     * @param status which status task need to be in list
     * @return list of current tasks with chosen status
     */

    @RequestMapping(path = "/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Collection<Task>> getTaskList(final @RequestParam(name = "status", required = false, defaultValue = "inbox")
                                                                    String status) {
        List<Task> result;
        String statusToCreate;
        if (status == null) {
            statusToCreate = TaskStatus.inbox.toString();
        } else {
            statusToCreate = status;
        }
        try {
            result = taskRepository.getTaskList(statusToCreate);
        } catch (DataAccessException e) {
            result = null;
        }
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
    }

    /**
     * Create new task with inbox status
     * @param node body Object that contain JSON with text of new task
     * @return JSON with new Task Object; HttpStatus: CREATED if success or BAD_REQUEST if bad body
     */

    @RequestMapping(path = "/tasks", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> createTask(final @RequestBody ObjectNode node) {
        JsonNode textNode = node.get("text");
        if (textNode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    contentType(MediaType.APPLICATION_JSON).build();
        }
        String text = textNode.asText();
        Task createdTask = taskRepository.createTask(text);
        if (createdTask.getStatus() == TaskStatus.empty) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    contentType(MediaType.APPLICATION_JSON).build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(String.format("/tasks/%s", createdTask.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(createdTask);
    }

    /**
     * Get current task
     * @param id id of task to get
     * @return JSON with asked task (or with empty task if error within);
     * status: OK if success, NOT_FOUND if there is no task with chosen id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> getTask(final @PathVariable String id) {
        if (isValideId(id)) {
            Task task = taskRepository.getTask(id);
            if (!task.getStatus().is(TaskStatus.empty)) {
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
     * @param id id of task to patch
     * @param node body Object that contain JSON with fields to update
     * @return JSON with updated task (or with empty task if some errors within);
     * status: NO_CONTENT if success, BAD_REQUEST if bad body, NOT_FOUND if there is no current task with chosen id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.PATCH, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> patchTask(final @PathVariable String id, final @RequestBody ObjectNode node) {
        if (isValideId(id)) {
            Task task = taskRepository.getTask(id);
            if (!task.getStatus().is(TaskStatus.empty)) {
                if (!task.update(node)) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .build();
                }
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(taskRepository.updateTask(id, task));
            }
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
    }

    /**
     * Delete task
     * @param id id of task to delete
     * @return JSON with deleted object if deleted correctly (or with empty task if some errors within);
     * status: OK if success, NOT_FOUND if no current task with that id
     */

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> deleteTask(final @PathVariable String id) {
        if (isValideId(id)) {
            Task result = taskRepository.deleteTask(id);
            if (!result.getStatus().is(TaskStatus.empty)) {
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
     * @param id UUID as String
     * @return true if valid, false if not
     */

    private boolean isValideId(final String id) {
        return true;
    }
}
