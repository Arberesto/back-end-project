package it.sevenbits.taskmanager.core.repository;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskRepositoryTest {

    private TaskFactory factory;
    private Task emptyTask;

    @Before
    public void SetUp(){
        factory = new TaskFactory();
        emptyTask = factory.getNewTask("null", "emptyTask", TaskStatus.empty);
    }

    /*

    @Test
    public void getTaskListTest1() {

        List<Task> result1 = new ArrayList<>();

        List<Task> result2 = new ArrayList<>();
        result2.add(factory.getNewTask(UUID.randomUUID().toString(),"firstTask", TaskStatus.done));
        result2.add(factory.getNewTask(UUID.randomUUID().toString(),"secondTask", TaskStatus.done));

        List<Task> result3 = new ArrayList<>();
        result3.add(factory.getNewTask(UUID.randomUUID().toString(),"thirdTask", TaskStatus.inbox));

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.query(anyString(), any(RowMapper.class), any())).
                thenReturn(result1, result2, result3);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);

        assertEquals(result1,taskRepository.getTaskList(TaskStatus.done.toString()));

        assertEquals(result2,taskRepository.getTaskList(TaskStatus.done.toString()));

        assertEquals(result3,taskRepository.getTaskList(TaskStatus.inbox.toString()));



    }


     // Test for Exception in method


    @Test
    public void getTaskListTest2() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.query(anyString(), any(RowMapper.class), any())).
                thenThrow(new RecoverableDataAccessException("Error!There is no 'dummy' row"));
        // there should be DataAccessException, but it's abstract, so I use it's subclass

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);
            List result = taskRepository.getTaskList(TaskStatus.inbox.toString());
            assertNull(result);
    }

    @Test
    public void createTaskTest1() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).thenReturn(1, 0);

        String text = "Hello";

        Task resultExpected = factory.getNewTask("null",text,TaskStatus.inbox);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);

        Task resultReal = taskRepository.createTask("   ");
        emptyTask.setCreatedAt(resultReal.getCreatedAt());
        emptyTask.setChangedAt(resultReal.getChangedAt());
        assertEquals(emptyTask, resultReal);


        resultReal = taskRepository.createTask(text);
        resultExpected.setId(resultReal.getId()); // id generated in repository,so i need them to be equal, obviously
        resultExpected.setCreatedAt(resultReal.getCreatedAt());
        resultExpected.setChangedAt(resultReal.getChangedAt());
        assertEquals(resultExpected, resultReal);

        resultReal = taskRepository.createTask(text);
        emptyTask.setCreatedAt(resultReal.getCreatedAt());
        emptyTask.setChangedAt(resultReal.getChangedAt());
        assertEquals(emptyTask, resultReal);
    }


     // Test for Exception in method


    @Test
    public void createTaskTest2() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).
                thenThrow(new RecoverableDataAccessException("Error!There is no 'dummy' row"));

        String text = "Hello";

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);

        Task resultReal = taskRepository.createTask(text);

        emptyTask.setCreatedAt(resultReal.getCreatedAt());
        emptyTask.setChangedAt(resultReal.getChangedAt());

        assertEquals(emptyTask,resultReal);
    }

    @Test
    public void getTaskTest1() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String text1 = "Hello";
        String text2 = "Hello 2";

        Task resultExpected1 = factory.getNewTask(id1, text1, TaskStatus.inbox);
        Task resultExpected2 = factory.getNewTask(id2, text2, TaskStatus.done);

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(anyString(), any(RowMapper.class), any())).
                thenReturn(resultExpected1, resultExpected2);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);

        Task resultReal1 = taskRepository.getTask(id1);
        resultExpected1.setCreatedAt(resultReal1.getCreatedAt());
        resultExpected1.setChangedAt(resultReal1.getChangedAt());

        Task resultReal2 = taskRepository.getTask(id2);
        resultExpected2.setCreatedAt(resultReal1.getCreatedAt());
        resultExpected2.setChangedAt(resultReal1.getChangedAt());

        assertEquals(resultExpected1,resultReal1);
        assertEquals(resultExpected2,resultReal2);
    }

    @Test
    public void getTaskTest2() {
        String id = UUID.randomUUID().toString();

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(anyString(), any(RowMapper.class), any())).
                thenThrow(new RecoverableDataAccessException("Error!There is no 'dummy' row"));

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);
        Task resultReal = taskRepository.getTask(id);

        emptyTask.setCreatedAt(resultReal.getCreatedAt());
        emptyTask.setChangedAt(resultReal.getChangedAt());
        assertEquals(emptyTask, resultReal);
    }

    @Test
    public void deleteTaskTest1() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String text1 = "Hello";
        String text2 = "Hello 2";

        Task resultExpected1 = factory.getNewTask(id1, text1, TaskStatus.inbox);
        Task resultExpected2 = factory.getNewTask(id2, text2, TaskStatus.done);

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(anyString(), any(RowMapper.class), any())).
                thenReturn(resultExpected1, resultExpected2);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);
        Task resultReal1 = taskRepository.deleteTask(id1);
        Task resultReal2 = taskRepository.deleteTask(id2);

        resultExpected1.setCreatedAt(resultReal1.getCreatedAt());
        resultExpected1.setChangedAt(resultReal1.getChangedAt());
        resultExpected2.setCreatedAt(resultReal1.getCreatedAt());
        resultExpected2.setChangedAt(resultReal1.getChangedAt());

        assertEquals(resultExpected1,resultReal1);
        assertEquals(resultExpected2,resultReal2);
    }

    @Test
    public void updateTaskTest1() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).thenReturn(1, 0);

        String id = UUID.randomUUID().toString();
        String text = "Hello";

        Task resultExpected = factory.getNewTask(id, text, TaskStatus.inbox);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);

        Task resultReal = taskRepository.updateTask(id,resultExpected);

        resultExpected.setId(resultReal.getId());
        resultExpected.setCreatedAt(resultReal.getCreatedAt());
        resultExpected.setChangedAt(resultReal.getChangedAt());

        assertEquals(resultExpected,resultReal);
    }


     // Test for Exception in method


    @Test
    public void updateTaskTest2() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).
                thenThrow(new RecoverableDataAccessException("Error!There is no 'dummy' row"));

        String id = UUID.randomUUID().toString();
        String text = "someTask";

        Task resultExpected = factory.getNewTask(id, text, TaskStatus.done);
        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);
        Task resultReal = taskRepository.updateTask(id,resultExpected);
        emptyTask.setCreatedAt(resultReal.getCreatedAt());
        emptyTask.setChangedAt(resultReal.getChangedAt());
        assertEquals(emptyTask, resultReal);
    }

    */
}
