package it.sevenbits.taskmanager.web.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@Controller
@RequestMapping("/tasks")

public class TasksController {

    private TaskRepository taskRepository;

    public TasksController(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
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

    @RequestMapping(method = RequestMethod.POST)
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
