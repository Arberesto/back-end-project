package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.task.Task;
import it.sevenbits.taskmanager.core.model.task.TaskFactory;
import it.sevenbits.taskmanager.core.model.task.TaskStatus;
import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.repository.tasks.PaginationSort;
import it.sevenbits.taskmanager.core.repository.tasks.PaginationTaskRepository;
import it.sevenbits.taskmanager.core.service.task.SimpleTaskService;
import it.sevenbits.taskmanager.core.service.user.UserService;
import it.sevenbits.taskmanager.web.model.requests.AddTaskRequest;
import it.sevenbits.taskmanager.web.model.requests.PatchTaskRequest;
import it.sevenbits.taskmanager.web.model.responce.GetTasksResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksControllerTest {
    private TasksController tasksController;
    private TaskFactory factory;
    private PaginationTaskRepository repository;
    private UserService userService;

    @Before
    public void SetUp() {
        factory = new TaskFactory();
    }


    @Test
    public void getTaskList_Inbox() {
        String id = UUID.randomUUID().toString();
        String id1 = UUID.randomUUID().toString();
        String owner = "owner1";
        List<Task> inboxList =  new ArrayList<>();
        Task newTask1 = factory.getNewTask(id, "firstTask",TaskStatus.inbox, owner);
        Task newTask2 = factory.getNewTask(id1, "firstTask1",TaskStatus.inbox, owner);
        inboxList.add(newTask1);
        inboxList.add(newTask2);
        GetTasksResponse response = new GetTasksResponse(inboxList,"inbox",
                "desc",1,25,2);
        repository = mock(PaginationTaskRepository.class);
        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt(), anyString())).
                thenReturn(response);
        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner,"","", null));

        tasksController = new TasksController(repository,null, userService);

        ResponseEntity<GetTasksResponse> responseOkInbox = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(response);

        assertEquals(responseOkInbox,
                tasksController.getTaskList(TaskStatus.inbox.toString(),
                        PaginationSort.DESC.toString(),1,25));

    }

    @Test
    public void getTaskList_Done() {
        String id = UUID.randomUUID().toString();
        String id1 = UUID.randomUUID().toString();
        String owner = "owner1";
        List<Task> doneList =  new ArrayList<>();
        Task newTask1 = factory.getNewTask(id, "secondTask",TaskStatus.done, owner);
        Task newTask2 = factory.getNewTask(id1, "thirdTask",TaskStatus.done, owner);
        doneList.add(newTask1);
        doneList.add(newTask2);
        GetTasksResponse response = new GetTasksResponse(doneList,"inbox",
                "desc",1,25,2);
        repository = mock(PaginationTaskRepository.class);
        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt(), anyString())).
                thenReturn(response);
        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner,"","", null));

        tasksController = new TasksController(repository,null, userService);

        ResponseEntity<GetTasksResponse> responseOkDone = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(response);

        assertEquals(responseOkDone,
                tasksController.getTaskList(TaskStatus.done.toString(),
                        PaginationSort.DESC.toString(),1,25));
    }

    @Test
    public void getTaskList_EmptyList() {
        String owner = "owner1";
        repository = mock(PaginationTaskRepository.class);

        GetTasksResponse response = new GetTasksResponse(new ArrayList<>(),"inbox",
                "desc",1,25,100);
        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt(), anyString())).
                thenReturn(response);
        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner,"","", null));
        tasksController = new TasksController(repository,null, userService);

        ResponseEntity<GetTasksResponse> responseOkEmpty = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(response);
        assertEquals(responseOkEmpty,
                tasksController.getTaskList("inbox","DESC",1,25));
    }

    @Test
    public void getTaskList_DifferentOwners() {
        String id = UUID.randomUUID().toString();
        String id1 = UUID.randomUUID().toString();
        String owner1 = "123456";
        String owner2 = "654321";
        List<Task> inboxList1 =  new ArrayList<>();
        List<Task> inboxList2 =  new ArrayList<>();
        Task newTask1 = factory.getNewTask(id, "firstTask",TaskStatus.inbox, owner1);
        Task newTask2 = factory.getNewTask(id1, "firstTask1",TaskStatus.inbox, owner2);
        inboxList1.add(newTask1);
        inboxList2.add(newTask2);
        GetTasksResponse response1 = new GetTasksResponse(inboxList1,"inbox",
                "desc",1,25,2);
        GetTasksResponse response2 = new GetTasksResponse(inboxList2,"inbox",
                "desc",1,25,2);
        repository = mock(PaginationTaskRepository.class);

        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt(), eq(owner1))).
                thenReturn(response1);
        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt(), eq(owner2))).
                thenReturn(response2);

        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner1,"","", null),
                new User(owner2,"","", null));

        tasksController = new TasksController(repository,null, userService);

        ResponseEntity<GetTasksResponse> responseOkInbox1 = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(response1);

        ResponseEntity<GetTasksResponse> responseOkInbox2 = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(response2);

        assertEquals(responseOkInbox1,
                tasksController.getTaskList("inbox","DESC",1,25));

        assertEquals(responseOkInbox2,
                tasksController.getTaskList("inbox","DESC",1,25));

    }

    @Test
    public void getTaskList_BadRequest() {
        repository = mock(PaginationTaskRepository.class);

        when(repository.getTaskList(anyString(), anyString(), anyInt(), anyInt(), anyString())).
                thenReturn(null);

        tasksController = new TasksController(repository,null, null);

        ResponseEntity<GetTasksResponse> responseBadRequest = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseBadRequest,
                tasksController.getTaskList("dnoe","DESC",1,25));
    }

    @Test
    public void createTask_Normal() {
        String id = UUID.randomUUID().toString();
        String owner = "somebody";
        AddTaskRequest request = new AddTaskRequest("firstTask");
        Task newTask = factory.getNewTask(id, "firstTask",TaskStatus.inbox, owner);
        repository = mock(PaginationTaskRepository.class);
        when(repository.createTask(anyString(), anyString())).thenReturn(newTask);
        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner,"","", null));

        tasksController = new TasksController(repository, null, userService);

        ResponseEntity<Task> responseReal = tasksController.createTask(request);

        ResponseEntity<Task> responseCreated = ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(String.format("/tasks/%s", newTask.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(newTask);

        assertEquals(responseCreated, responseReal);
    }

    @Test
    public void createTask_FromNull() {
        repository = mock(PaginationTaskRepository.class);
        when(repository.createTask(anyString(), anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null, null);

        ResponseEntity<Task> responseBadRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).build();

        assertEquals(responseBadRequest,tasksController.createTask(null));
    }

    @Test
    public void createTask_FromEmptyString() {
        AddTaskRequest request = new AddTaskRequest("");
        repository = mock(PaginationTaskRepository.class);
        when(repository.createTask(anyString(), anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null, null);

        ResponseEntity<Task> responseBadRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).build();

        assertEquals(responseBadRequest,tasksController.createTask(request));
    }

    @Test
    public void getTask_Normal() {
        String id = UUID.randomUUID().toString();
        String owner = "123456";
        Task newTask = factory.getNewTask(id, "someTask",TaskStatus.inbox, owner);

        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(newTask);

        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner,"","", null));

        tasksController = new TasksController(repository, null, userService);

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

        tasksController = new TasksController(repository, null, null);

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

        tasksController = new TasksController(repository, null, null);

        ResponseEntity<Task> responseInvalidId = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseInvalidId,tasksController.getTask(id1));
    }

    @Test
    public void getTask_Forbidden() {
        String id = UUID.randomUUID().toString();
        String owner1 = "123456";
        String owner2 = "654321";
        Task newTask = factory.getNewTask(id, "someTask",TaskStatus.inbox, owner1);
        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(newTask);
        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner2,"","", null));
        tasksController = new TasksController(repository, null, userService);

        ResponseEntity<Task> responseForbidden = ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseForbidden, tasksController.getTask(id));
    }

    @Test
    public void patchTask_Normal() {
        String id = UUID.randomUUID().toString();
        String owner = "123456";
        Task startTask = factory.getNewTask(id, "startTask", TaskStatus.inbox, owner);
        Task updatedTask = factory.getNewTask(id, "updatedTask", TaskStatus.done,
                startTask.getCreatedAt(), owner);

        PatchTaskRequest request = new PatchTaskRequest("updatedTask","done");

        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner,"","", null));
        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(startTask);
        when(repository.updateTask(anyString(),any(Task.class))).thenReturn(updatedTask);
        SimpleTaskService service = mock(SimpleTaskService.class);
        when(service.update(any(Task.class), any())).thenReturn(updatedTask);


        tasksController = new TasksController(repository, service, userService);

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

        tasksController = new TasksController(repository, service, null);

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

        tasksController = new TasksController(repository, service, null);

        ResponseEntity<Task> responseInvalidId = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseInvalidId, tasksController.patchTask(id1, request));
    }

    @Test
    public void patchTask_BadRequest() {
        String id = UUID.randomUUID().toString();
        String owner = "123456";
        Task startTask = factory.getNewTask(id, "startTask", TaskStatus.inbox, owner);
        PatchTaskRequest requestBad = new PatchTaskRequest("updatedTask","dnoe");

        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner,"","", null));
        repository = mock(PaginationTaskRepository.class);
        SimpleTaskService service = mock(SimpleTaskService.class);

        when(service.update(any(Task.class), any())).thenReturn(null);
        when(repository.getTask(anyString())).thenReturn(startTask);
        when(repository.updateTask(anyString(),any(Task.class))).thenReturn(null);

        tasksController = new TasksController(repository, service, userService);

        ResponseEntity<Task> responseBadRequest = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseBadRequest,tasksController.patchTask(id,requestBad));
    }

    @Test
    public void patchTask_Forbidden() {
        String id = UUID.randomUUID().toString();
        String owner1 = "123456";
        String owner2 = "654321";
        Task startTask = factory.getNewTask(id, "startTask", TaskStatus.inbox, owner1);
        PatchTaskRequest requestBad = new PatchTaskRequest("updatedTask","done");

        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner2,"","", null));
        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(startTask);
        when(repository.updateTask(anyString(),any(Task.class))).thenReturn(null);

        tasksController = new TasksController(repository, null, userService);

        ResponseEntity<Task> responseForbidden = ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseForbidden, tasksController.patchTask(id,requestBad));
    }

    @Test
    public void deleteTask_Normal() {

        String id = UUID.randomUUID().toString();
        String text = "deleted task";
        String owner = "somebody";
        Task deletedTask = factory.getNewTask(id, text, TaskStatus.inbox, owner);

        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner,"","", null));
        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(deletedTask);
        when(repository.deleteTask(anyString())).thenReturn(deletedTask);

        tasksController = new TasksController(repository, null, userService);

        ResponseEntity<Task> responseOk = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(deletedTask);

        assertEquals(responseOk,tasksController.deleteTask(id));
    }

    @Test
    public void deleteTask_NotFound() {

        String id = "1234568-3333-1234-1234-123456789012";
        String id1 = "1234568-1234-1234-1234-123456789012";

        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(null);

        tasksController = new TasksController(repository, null, null);

        ResponseEntity<Task> responseNotFound =  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseNotFound,tasksController.deleteTask(id));
        assertEquals(responseNotFound,tasksController.deleteTask(id1));
    }

    @Test
    public void deleteTask_Forbidden() {

        String id = UUID.randomUUID().toString();
        String text = "deleted task";
        String owner1 = "123456";
        String owner2 = "654321";
        Task deletedTask = factory.getNewTask(id, text, TaskStatus.inbox, owner1);

        userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new User(owner2,"","", null));
        repository = mock(PaginationTaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(deletedTask);
        when(repository.deleteTask(anyString())).thenReturn(deletedTask);

        tasksController = new TasksController(repository, null, userService);

        ResponseEntity<Task> responseForbidden = ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseForbidden,tasksController.deleteTask(id));
    }
}
