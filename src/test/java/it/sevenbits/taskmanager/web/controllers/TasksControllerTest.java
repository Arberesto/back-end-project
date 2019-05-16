package it.sevenbits.taskmanager.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksControllerTest {
    private TasksController tasksController;
    private TaskFactory factory;
    private Task emptyTask;
    private ObjectMapper mapper;
    @Before
    public void SetUp() {
        factory = new TaskFactory();
        emptyTask = factory.getNewTask("null","emptyTask",TaskStatus.empty);
        mapper = new ObjectMapper();
    }

    @Test
    public void getTaskListTest() {

    }

    @Test
    public void createTaskTest() {

    }

    @Test
    public void getTaskTest() {
        String id = UUID.randomUUID().toString();
        String id1 = "1234568-1234-1234-1234-123456789012";
        Task newTask = factory.getNewTask(id, "someTask",TaskStatus.inbox);

        TaskRepository repository = mock(TaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(newTask, emptyTask);

        tasksController = new TasksController(repository);

        ResponseEntity<Task> responseOk  = ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(newTask);
        ResponseEntity<Task> responseNotFound = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseNotFound,tasksController.getTask(id1));
        assertEquals(responseOk,tasksController.getTask(id));
        assertEquals(responseNotFound,tasksController.getTask(id));
    }

    @Test
    public void patchTaskTest() {
        String id = UUID.randomUUID().toString();
        String id1 = "1234568-1234-1234-1234-123456789012";

        Task startTask = factory.getNewTask(id, "startTask", TaskStatus.inbox);
        Task updatedTask = factory.getNewTask(id, "updatedTask", TaskStatus.done);

        TaskRepository repository = mock(TaskRepository.class);
        when(repository.getTask(anyString())).thenReturn(startTask, emptyTask,startTask);
        when(repository.updateTask(anyString(),any(Task.class))).thenReturn(updatedTask);

        tasksController = new TasksController(repository);

        ObjectNode node = mapper.createObjectNode();
        node.put("text","updatedTask");
        node.put("status","done");
        ObjectNode badNode = mapper.createObjectNode();
        badNode.put("text","updatedTask");
        badNode.put("status","done");
        badNode.put("id","1234568-1234-1234-1234-123456789012");

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

        assertEquals(responseNotFound,tasksController.patchTask(id1,node));
        assertEquals(responseOk,tasksController.patchTask(id,node));
        assertEquals(responseNotFound,tasksController.patchTask(id,node));
        assertEquals(responseBadRequest,tasksController.patchTask(id,badNode));
    }

    @Test
    public void deleteTaskTest() {

        String id = UUID.randomUUID().toString();
        String id1 = "1234568-1234-1234-1234-123456789012";
        String text = "deleted task";
        Task deletedTask = factory.getNewTask(id, text, TaskStatus.inbox);

        TaskRepository repository = mock(TaskRepository.class);
        when(repository.deleteTask(anyString())).thenReturn(deletedTask, emptyTask);

        tasksController = new TasksController(repository);

        ResponseEntity<Task> responseOk = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(deletedTask);

        ResponseEntity<Task> responseNotFound =  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build();

        assertEquals(responseOk,repository.deleteTask(id));
        assertEquals(responseNotFound,repository.deleteTask(id));
        assertEquals(responseNotFound,repository.deleteTask(id1));
    }
}
