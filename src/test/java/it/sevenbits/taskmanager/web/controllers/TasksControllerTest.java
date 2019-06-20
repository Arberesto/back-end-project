package it.sevenbits.taskmanager.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.core.model.Task.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksControllerTest {
    private TasksController tasksController;
    private TaskFactory factory;
    @Before
    public void SetUp() {
        factory = new TaskFactory();
    }


    @Test
    public void getTaskList_Normal() {
        String id = UUID.randomUUID().toString();
        List<Task> emptyList = new ArrayList<>();
        List<Task> inboxList =  new ArrayList<>();
        List<Task> doneList =  new ArrayList<>();
        Task newTask = factory.getNewTask(id, "firstTask",TaskStatus.inbox);
        Task newTask1 = factory.getNewTask(id, "secondTask",TaskStatus.done);
        Task newTask2 = factory.getNewTask(id, "thirdTask",TaskStatus.done);
        inboxList.add(newTask);
        doneList.add(newTask1);
        doneList.add(newTask2);
        TaskRepository repository = mock(TaskRepository.class);

        when(repository.getTaskList(anyString())).
                thenReturn(inboxList, inboxList, emptyList, doneList, null);

        tasksController = new TasksController(repository,null);

        ResponseEntity<Collection<Task>> responseBadRequest = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        ResponseEntity<Collection<Task>> responseOkInbox = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(inboxList);

        ResponseEntity<Collection<Task>> responseOkDone = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(doneList);

        ResponseEntity<Collection<Task>> responseOkEmpty = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(emptyList);
        assertEquals(responseOkInbox, tasksController.getTaskList(null,null,null,25));
        assertEquals(responseOkInbox, tasksController.getTaskList("inbox",null,null,25));
        assertEquals(responseOkEmpty,tasksController.getTaskList("cool",null,null,25));
        assertEquals(responseOkDone,tasksController.getTaskList("done",null,null,25));
        assertEquals(responseBadRequest,tasksController.getTaskList("done",null,null,25));

    }

    @Test
    public void createTask_Normal() {
        String id = UUID.randomUUID().toString();
        AddTaskRequest request = new AddTaskRequest("firstTask");
        Task newTask = factory.getNewTask(id, "firstTask",TaskStatus.inbox);
        TaskRepository repository = mock(TaskRepository.class);
        when(repository.createTask(anyString())).thenReturn(newTask, null);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseBadRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).build();

        ResponseEntity<Task> responseCreated = ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(String.format("/tasks/%s", newTask.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(newTask);

        assertEquals(responseCreated, tasksController.createTask(request));
        assertEquals(responseBadRequest,tasksController.createTask(request));
        assertEquals(responseBadRequest,tasksController.createTask(null));
    }

    @Test
    public void createTask_FromNull() {
        String id = UUID.randomUUID().toString();
        Task newTask = factory.getNewTask(id, "firstTask",TaskStatus.inbox);
        TaskRepository repository = mock(TaskRepository.class);
        when(repository.createTask(anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseBadRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).build();

        assertEquals(responseBadRequest,tasksController.createTask(null));
    }

    @Test
    public void getTask_Normal() {
        String id = UUID.randomUUID().toString();
        String id1 = "1234568-1234-1234-1234-123456789012";
        Task newTask = factory.getNewTask(id, "someTask",TaskStatus.inbox);

        TaskRepository repository = mock(TaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(newTask, null);

        tasksController = new TasksController(repository, null);

        ResponseEntity<Task> responseOk  = ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(newTask);
        ResponseEntity<Task> responseNotFound = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        //assertEquals(responseNotFound,tasksController.getTask(id1));
        assertEquals(responseOk,tasksController.getTask(id));
        assertEquals(responseNotFound,tasksController.getTask(id));
    }

    @Test
    public void patchTask_Normal() {
        String id = UUID.randomUUID().toString();
        String id1 = "1234568-1234-1234-1234-123456789012";

        Task startTask = factory.getNewTask(id, "startTask", TaskStatus.inbox);
        Task updatedTask = factory.getNewTask(id, "updatedTask", TaskStatus.done);

        PatchTaskRequest request = new PatchTaskRequest("updatedTask","done");
        PatchTaskRequest requestBad = new PatchTaskRequest("updatedTask","dnoe");

        TaskRepository repository = mock(TaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(startTask, null, startTask);
        when(repository.updateTask(anyString(),any(Task.class))).thenReturn(updatedTask);

        tasksController = new TasksController(repository,new SimpleTaskService(new TaskFactory()));

        ResponseEntity<Task> responseOk  = ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(updatedTask);
        ResponseEntity<Task> responseNotFound = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        ResponseEntity<Task> responseBadRequest = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        //assertEquals(responseNotFound,tasksController.patchTask(id1, request));

        assertEquals(responseOk,tasksController.patchTask(id, request));
        assertEquals(responseNotFound,tasksController.patchTask(id, request));
        assertEquals(responseBadRequest,tasksController.patchTask(id,requestBad));
    }

    @Test
    public void deleteTask_Normal() {

        String id = UUID.randomUUID().toString();
        String text = "deleted task";
        Task deletedTask = factory.getNewTask(id, text, TaskStatus.inbox);

        TaskRepository repository = mock(TaskRepository.class);
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

        TaskRepository repository = mock(TaskRepository.class);
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
