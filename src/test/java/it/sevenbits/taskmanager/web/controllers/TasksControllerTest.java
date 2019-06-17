package it.sevenbits.taskmanager.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sevenbits.taskmanager.core.model.TaskFactory.Task;
import it.sevenbits.taskmanager.core.model.TaskFactory.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import org.junit.Before;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

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
    /*

    @Test
    public void getTaskListTest() {
        String id = UUID.randomUUID().toString();
        ObjectNode node = mapper.createObjectNode();
        node.put("text","firstTask");
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

        tasksController = new TasksController(repository);

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
    public void createTaskTest() {
        String id = UUID.randomUUID().toString();
        ObjectNode node = mapper.createObjectNode();
        node.put("text","firstTask");
        Task newTask = factory.getNewTask(id, "firstTask",TaskStatus.inbox);
        TaskRepository repository = mock(TaskRepository.class);
        when(repository.createTask(anyString())).thenReturn(newTask, emptyTask);

        tasksController = new TasksController(repository);

        ResponseEntity<Task> responseBadRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST).
                contentType(MediaType.APPLICATION_JSON).build();

        ResponseEntity<Task> responseCreated = ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(String.format("/tasks/%s", newTask.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(newTask);

        assertEquals(responseCreated, tasksController.createTask(node));
        assertEquals(responseBadRequest,tasksController.createTask(node));
        assertEquals(responseBadRequest,tasksController.createTask(null));
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

        assertEquals(responseOk,tasksController.deleteTask(id));
        assertEquals(responseNotFound,tasksController.deleteTask(id));
        assertEquals(responseNotFound,tasksController.deleteTask(id1));
    }
    */
}
