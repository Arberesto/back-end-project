package it.sevenbits.taskmanager.web.controllers;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksControllerTest {
    private TasksController tasksController;
    private TaskFactory factory;
    private Task emptyTask;
    @Before
    public void SetUp() {
        factory = new TaskFactory();
        emptyTask = factory.getNewTask("null","emptyTask",TaskStatus.empty);
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
}
