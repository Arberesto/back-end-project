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


@Controller
@RequestMapping("/tasks/{id}")
public class TaskController {

    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Task> getTask(@PathVariable String id) {
        Task task = taskRepository.getTask(id);
        if (task.getStatus() == TaskStatus.empty) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(task);
    }

    @RequestMapping(method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<Task> patchTask(@PathVariable String id, @RequestBody ObjectNode node) {
        Task task = taskRepository.getTask(id);
        if (task.getStatus() == TaskStatus.empty) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }
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

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Task> deleteTask(@PathVariable String id) {

        Task result = taskRepository.deleteTask(id);
        if (result.getStatus() == TaskStatus.empty) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(result);
    }
}
