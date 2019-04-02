package it.sevenbits.taskmanager.web.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller of actions with current task: get, patch, delete
 */

@Controller
@RequestMapping("/tasks/{id}")
public class TaskController {

    private TaskRepository taskRepository;

    /**
     * Public constructor
     * @param taskRepository TaskRepository to use
     */

    public TaskController(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get current task
     * @param id id of task to get
     * @return JSON with asked task (or with empty task if error within);
     * status: OK if success, NOT_FOUND if there is no task with chosen id
     */

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> getTask(final @PathVariable String id) {
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

    /**
     * Patch some fields of task
     * @param id id of task to patch
     * @param node body Object that contain JSON with fields to update
     * @return JSON with updated task (or with empty task if some errors within);
     * status: NO_CONTENT if success, BAD_REQUEST if bad body, NOT_FOUND if there is no current task with chosen id
     */

    @RequestMapping(method = RequestMethod.PATCH, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> patchTask(final @PathVariable String id, final @RequestBody ObjectNode node) {
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

    /**
     * Delete task
     * @param id id of task to delete
     * @return JSON with deleted object if deleted correctly (or with empty task if some errors within);
     * status: OK if success, NOT_FOUND if no current task with that id
     */

    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> deleteTask(final @PathVariable String id) {

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
