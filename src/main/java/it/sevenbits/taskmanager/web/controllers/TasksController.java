package it.sevenbits.taskmanager.web.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Collection;

/**
 * Controller for creating tasks and get list of current tasks
 */

@Controller
@RequestMapping("/tasks")

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

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Collection<Task>> getTaskList(final @RequestParam(name = "status", required = false)
                                                                    String status) {

        System.out.println("Status to get: " + status);
        if (status == null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(taskRepository.getTaskList(TaskStatus.inbox.toString()));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(taskRepository.getTaskList(status));
    }

    /**
     * Create new task with inbox status
     * @param node body Object that contain JSON with text of new task
     * @return JSON with new Task Object; HttpStatus: CREATED if success or BAD_REQUEST if bad body
     */

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> createTask(final @RequestBody ObjectNode node) {
        String text = node.get("text").asText();
        if (text == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    contentType(MediaType.APPLICATION_JSON).build();
        }
        Task createdTask = taskRepository.createTask(text);
        if (createdTask == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    contentType(MediaType.APPLICATION_JSON).build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(String.format("/tasks/%s", createdTask.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(createdTask);
    }
}
