package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
    public ResponseEntity<Collection<Task>> getTaskList() {
        System.out.println("Example log");
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(taskRepository.getTaskList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Task> createTask(final @RequestParam("text") String text,
                                           final HttpServletResponse response) {
        Task createdTask = taskRepository.createTask(text);
        if (createdTask == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    contentType(MediaType.APPLICATION_JSON).build();
        }
        Cookie cookie = new Cookie("sessionId", createdTask.getId());
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(String.format("/tasks/%s", createdTask.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(createdTask);
    }
}
