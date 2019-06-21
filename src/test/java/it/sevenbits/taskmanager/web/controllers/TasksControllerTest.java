package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.core.model.Task.TaskFactory;
import it.sevenbits.taskmanager.core.model.Task.TaskStatus;
import it.sevenbits.taskmanager.core.repository.PaginationTaskRepository;
import it.sevenbits.taskmanager.core.service.SimpleTaskService;
import it.sevenbits.taskmanager.web.model.AddTaskRequest;
import it.sevenbits.taskmanager.web.model.PatchTaskRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksControllerTest {
    private TasksController tasksController;
    private TaskFactory factory;
    private PaginationTaskRepository repository;

    @Before
    public void SetUp() {
        factory = new TaskFactory();
    }


    @Test
    public void getTaskList_Inbox() {
        String id = UUID.randomUUID().toString();
        String id1 = UUID.randomUUID().toString();
        List<Task> inboxList =  new ArrayList<>();
        Task newTask1 = factory.getNewTask(id, "firstTask",TaskStatus.inbox);
        Task newTask2 = factory.getNewTask(id1, "firstTask1",TaskStatus.inbox);
        inboxList.add(newTask1);
        inboxList.add(newTask2);
        repository = mock(PaginationTaskRepository.class);

        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt())).
                thenReturn(inboxList);

        tasksController = new TasksController(repository,null);

        ResponseEntity<Collection<Task>> responseOkInbox = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(inboxList);

        assertEquals(responseOkInbox,
                tasksController.getTaskList("inbox","desc",1,25));

    }

    @Test
    public void getTaskList_Done() {
        String id = UUID.randomUUID().toString();
        String id1 = UUID.randomUUID().toString();
        List<Task> doneList =  new ArrayList<>();
        Task newTask1 = factory.getNewTask(id, "secondTask",TaskStatus.done);
        Task newTask2 = factory.getNewTask(id1, "thirdTask",TaskStatus.done);
        doneList.add(newTask1);
        doneList.add(newTask2);
        repository = mock(PaginationTaskRepository.class);

        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt())).
                thenReturn(doneList);

        tasksController = new TasksController(repository,null);

        ResponseEntity<Collection<Task>> responseOkDone = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(doneList);

        assertEquals(responseOkDone,
                tasksController.getTaskList("done","desc",1,25));
    }

    @Test
    public void getTaskList_EmptyList() {
        String id = UUID.randomUUID().toString();
        List<Task> emptyList = new ArrayList<>();

        repository = mock(PaginationTaskRepository.class);

        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt())).
                thenReturn(emptyList);

        tasksController = new TasksController(repository,null);

        ResponseEntity<Collection<Task>> responseOkEmpty = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(emptyList);
        assertEquals(responseOkEmpty,
                tasksController.getTaskList("inbox","desc",1,25));
    }

    @Test
    public void getTaskList_BadRequest() {
        repository = mock(PaginationTaskRepository.class);

        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt())).
                thenReturn(null);

        tasksController = new TasksController(repository,null);

        ResponseEntity<Collection<Task>> responseBadRequest = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseBadRequest,
                tasksController.getTaskList("dnoe","desc",1,25));
    }

    @Test
    public void createTask_Normal() {
        String id = UUID.randomUUID().toString();
        AddTaskRequest request = new AddTaskRequest("firstTask");
        Task newTask = factory.getNewTask(id, "firstTask",TaskStatus.inbox);
        repository = mock(PaginationTaskRepository.class);
        when(repository.createTask(anyString())).thenReturn(newTask);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseCreated = ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(String.format("/tasks/%s", newTask.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(newTask);

        assertEquals(responseCreated, tasksController.createTask(request));
    }

    @Test
    public void createTask_FromNull() {
        repository = mock(PaginationTaskRepository.class);
        when(repository.createTask(anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseBadRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).build();

        assertEquals(responseBadRequest,tasksController.createTask(null));
    }

    @Test
    public void createTask_FromEmptyString() {
        AddTaskRequest request = new AddTaskRequest("");
        repository = mock(PaginationTaskRepository.class);
        when(repository.createTask(anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseBadRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).build();

        assertEquals(responseBadRequest,tasksController.createTask(request));
    }

    @Test
    public void getTask_Normal() {
        String id = UUID.randomUUID().toString();
        Task newTask = factory.getNewTask(id, "someTask",TaskStatus.inbox);

        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(newTask);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseOk  = ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(newTask);
        assertEquals(responseOk, tasksController.getTask(id));
    }

    @Test
    public void getTask_NotFound() {
        String id = UUID.randomUUID().toString();

        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseNotFound = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();
        assertEquals(responseNotFound,tasksController.getTask(id));
    }

    @Test
    public void getTask_InvalidId() {
        String id1 = "1234568-1234-1234-1234-123456789012";

        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseNotFound = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseNotFound,tasksController.getTask(id1));
    }

    @Test
    public void patchTask_Normal() {
        String id = UUID.randomUUID().toString();

        Task startTask = factory.getNewTask(id, "startTask", TaskStatus.inbox);
        Task updatedTask = factory.getNewTask(id, "updatedTask", TaskStatus.done,
                startTask.getCreatedAt());

        PatchTaskRequest request = new PatchTaskRequest("updatedTask","done");

        repository = mock(PaginationTaskRepository.class);
        SimpleTaskService service = mock(SimpleTaskService.class);

        when(service.update(any(Task.class), any())).thenReturn(updatedTask);
        when(repository.getTask(anyString())).thenReturn(startTask);
        when(repository.updateTask(anyString(),any(Task.class))).thenReturn(updatedTask);

        tasksController = new TasksController(repository, service);

        ResponseEntity<Task> responseOk  = ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(updatedTask);

        assertEquals(responseOk,tasksController.patchTask(id, request));
    }

    @Test
    public void patchTask_NotFound() {
        String id = UUID.randomUUID().toString();

        PatchTaskRequest request = new PatchTaskRequest("updatedTask","done");

        repository = mock(PaginationTaskRepository.class);
        SimpleTaskService service = mock(SimpleTaskService.class);

        when(repository.getTask(anyString())).thenReturn(null);

        tasksController = new TasksController(repository, service);

        ResponseEntity<Task> responseNotFound = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseNotFound,tasksController.patchTask(id, request));
    }

    @Test
    public void patchTask_InvalidId() {
        String id1 = "1234568-1234-1234-1234-123456789012";

        PatchTaskRequest request = new PatchTaskRequest("updatedTask","done");

        repository = mock(PaginationTaskRepository.class);
        SimpleTaskService service = mock(SimpleTaskService.class);

        tasksController = new TasksController(repository, service);

        ResponseEntity<Task> responseNotFound = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseNotFound, tasksController.patchTask(id1, request));
    }

    @Test
    public void patchTask_BadRequest() {
        String id = UUID.randomUUID().toString();

        Task startTask = factory.getNewTask(id, "startTask", TaskStatus.inbox);
        PatchTaskRequest requestBad = new PatchTaskRequest("updatedTask","dnoe");

        repository = mock(PaginationTaskRepository.class);
        SimpleTaskService service = mock(SimpleTaskService.class);

        when(service.update(any(Task.class), any())).thenReturn(null);
        when(repository.getTask(anyString())).thenReturn(startTask);
        when(repository.updateTask(anyString(),any(Task.class))).thenReturn(null);

        tasksController = new TasksController(repository, service);

        ResponseEntity<Task> responseBadRequest = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseBadRequest,tasksController.patchTask(id,requestBad));
    }

    @Test
    public void deleteTask_Normal() {

        String id = UUID.randomUUID().toString();
        String text = "deleted task";
        Task deletedTask = factory.getNewTask(id, text, TaskStatus.inbox);

        repository = mock(PaginationTaskRepository.class);
        when(repository.deleteTask(anyString())).thenReturn(deletedTask);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseOk = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(deletedTask);

        assertEquals(responseOk,tasksController.deleteTask(id));
    }

    @Test
    public void deleteTask_NotFound() {

        String id = UUID.randomUUID().toString();
        String id1 = "1234568-1234-1234-1234-123456789012";

        repository = mock(PaginationTaskRepository.class);
        when(repository.deleteTask(anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseNotFound =  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseNotFound,tasksController.deleteTask(id));
        assertEquals(responseNotFound,tasksController.deleteTask(id1));
    }
}
