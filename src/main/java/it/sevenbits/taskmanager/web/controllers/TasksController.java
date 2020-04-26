package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.task.Task;
import it.sevenbits.taskmanager.core.model.task.TaskStatus;
import it.sevenbits.taskmanager.core.repository.tasks.PaginationSort;
import it.sevenbits.taskmanager.core.repository.tasks.PaginationTaskRepository;
import it.sevenbits.taskmanager.core.service.task.TaskService;
import it.sevenbits.taskmanager.core.service.user.UserService;
import it.sevenbits.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.taskmanager.web.model.requests.PatchTaskRequest;

import it.sevenbits.taskmanager.web.model.responce.GetTasksResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.net.URI;

/**
 * Controller for creating tasks and get list of current tasks
 */

@Controller
@RequestMapping(path = "/tasks")
public class TasksController {

    private PaginationTaskRepository taskRepository;
    private Logger logger;
    private TaskService taskService;
    private UserService userService;
    private final String idValidationPattern = "^[\\da-fA-F]{8}-[\\da-fA-F]{4}-[\\da-fA-F]{4}-[\\da-fA-F]{4}-[\\da-fA-F]{12}$";

    /**
     * Public constructor
     *
     * @param taskRepository repository for tasks to use
     * @param taskService service that work with Task Objects
     * @param userService service that work with User Objects
     */

    public TasksController(final PaginationTaskRepository taskRepository, final TaskService taskService,
                           final UserService userService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.userService = userService;
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

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<GetTasksResponse> getTaskList(
            @RequestParam(name = "status", required = false,
                    defaultValue = "inbox") final String status,
            @RequestParam(name = "order", required = false,
                    defaultValue = "desc") final String order,
            @Min(1) @RequestParam(name = "page", required = false,
                    defaultValue = "1") final Integer page,
            @Min(10) @Max(50) @RequestParam(name = "size", required = false,
                    defaultValue = "25") final Integer size) {
        try {
            if (TaskStatus.resolveString(status) != null &&
                    PaginationSort.resolveString(order) != null) {

                String statusToGet = TaskStatus.resolveString(status).toString();
                String orderToGet = PaginationSort.resolveString(order).toString();
                String owner = userService.getCurrentUser().getId();
                GetTasksResponse result = taskRepository.getTaskList(statusToGet, orderToGet, page, size, owner);
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
    }

    /**
     * Create new task with inbox status
     *
     * @param request Model that contains parameters for updating task
     * @return JSON with new Task Object; HttpStatus: CREATED if success or BAD_REQUEST if bad body
     */

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> createTask(@RequestBody final AddTaskRequest request) {
        try {
            String owner = userService.getCurrentUser().getId();
            Task createdTask = taskRepository.createTask(request.getText(), owner);
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
     * Get task by id
     *
     * @param id id of task to get
     * @return JSON with asked task (or with empty task if error within);
     * HttpStatus: OK if success, NOT_FOUND if there is no task with chosen id, FORBIDDEN if trying to get not your task
     */

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> getTask(@Valid @Pattern(
            regexp = idValidationPattern) @PathVariable final String id) {
        Task task = taskRepository.getTask(id);
        if (task != null) {
            String owner = userService.getCurrentUser().getId();
            if (task.getOwner().equals(owner)) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(task);
            } else {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
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
     * Patch some fields of task
     *
     * @param id id of task to patch
     * @param request body Object that contain JSON with fields to update
     * @return JSON with updated task (or with empty task if some errors within);
     * HttpStatus: NO_CONTENT if success, BAD_REQUEST if bad body,
     * NOT_FOUND if there is no current task with chosen id
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> patchTask(@Valid @Pattern(
            regexp = idValidationPattern) @PathVariable final String id,
                                          @RequestBody final PatchTaskRequest request) {
        Task task = taskRepository.getTask(id);
        if (task != null) {
            String owner = userService.getCurrentUser().getId();
            if (task.getOwner().equals(owner)) {
                Task result = taskRepository.updateTask(id, taskService.update(task, request));
                if (result != null) {
                    return ResponseEntity
                            .status(HttpStatus.NO_CONTENT)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(result);
                } else {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .build();
                }
            } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
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
     * HttpStatus: OK if success, NOT_FOUND if no current task with that id
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Task> deleteTask(@Valid @Pattern(
            regexp = idValidationPattern) @PathVariable final String id) {
        Task task = taskRepository.getTask(id);
        if (task != null) {
            String owner = userService.getCurrentUser().getId();
            if (!task.getOwner().equals(owner)) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .build();
            }
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
}
