package it.sevenbits.taskmanager.core.repository;
import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.core.model.Task.TaskFactory;
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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskRepositoryTest {

    private TaskFactory factory;
    @Before
    public void SetUp(){
        factory = new TaskFactory();
    }


    @Test
    public void getTaskList_Normal() {

        List<Task> result2 = new ArrayList<>();
        result2.add(factory.getNewTask(UUID.randomUUID().toString(),"firstTask", TaskStatus.done));
        result2.add(factory.getNewTask(UUID.randomUUID().toString(),"secondTask", TaskStatus.done));

        List<Task> result3 = new ArrayList<>();
        result3.add(factory.getNewTask(UUID.randomUUID().toString(),"thirdTask", TaskStatus.inbox));

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.query(anyString(), any(RowMapper.class), any())).
                thenReturn(result2, result3);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);

        assertEquals(result2,taskRepository.getTaskList(TaskStatus.done.toString()));

        assertEquals(result3,taskRepository.getTaskList(TaskStatus.inbox.toString()));



    }

    @Test
    public void getTaskList_EmptyList() {
        List<Task> result1 = new ArrayList<>();

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.query(anyString(), any(RowMapper.class), any())).
                thenReturn(result1);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);

        assertEquals(result1,taskRepository.getTaskList(TaskStatus.done.toString()));
    }

    @Test
    public void getTaskList_JDBCError() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.query(anyString(), any(RowMapper.class), any())).
                thenThrow(new RecoverableDataAccessException("Error!There is no 'dummy' row"));

        // there should be DataAccessException, but it's abstract, so I use it's subclass

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);
            List result = taskRepository.getTaskList(TaskStatus.inbox.toString());
            assertNull(result);
    }

    @Test
    public void createTask_Normal() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).thenReturn(1);

        String text = "Hello";


        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);

        Task resultReal = taskRepository.createTask(text);

        // id generated in repository,so i need them to be equal, obviously

        Task resultExpected = factory.getNewTask(resultReal.getId(),
                text,TaskStatus.inbox, resultReal.getCreatedAt(),resultReal.getChangedAt());

        assertEquals(resultExpected, resultReal);
    }

    @Test
    public void createTask_NotCreatedInJDBC() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).thenReturn(0);

        String text = "Hello";


        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);

        Task resultReal = taskRepository.createTask(text);
        assertNull(resultReal);
    }

    @Test
    public void createTask_FromEmptyString() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).thenReturn(1, 0);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);

        Task resultReal = taskRepository.createTask("   ");
        assertNull(resultReal);
    }


    @Test
    public void createTask_JDBCError() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).
                thenThrow(new RecoverableDataAccessException("Error!There is no 'dummy' row"));

        String text = "Hello";

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);

        Task resultReal = taskRepository.createTask(text);

        assertNull(resultReal);
    }

    @Test
    public void getTask_Normal() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String text1 = "Hello";
        String text2 = "Hello 2";

        Task resultExpected1 = factory.getNewTask(id1, text1, TaskStatus.inbox);
        Task resultExpected2 = factory.getNewTask(id2, text2, TaskStatus.done);

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(anyString(), any(RowMapper.class), any())).
                thenReturn(resultExpected1, resultExpected2);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations,factory);

        Task resultReal1 = taskRepository.getTask(id1);

        Task resultReal2 = taskRepository.getTask(id2);

        resultExpected1 = factory.getNewTask(id1, text1, TaskStatus.inbox,
                resultExpected1.getCreatedAt(), resultReal1.getChangedAt());

        resultExpected2 = factory.getNewTask(id2, text2, TaskStatus.done,
                resultExpected2.getCreatedAt(), resultReal2.getChangedAt());

        assertEquals(resultExpected1,resultReal1);
        assertEquals(resultExpected2,resultReal2);
    }

    @Test
    public void getTask_JDBCError() {
        String id = UUID.randomUUID().toString();

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(anyString(), any(RowMapper.class), any())).
                thenThrow(new RecoverableDataAccessException("Expected error!There is no 'dummy' row"));

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);
        Task resultReal = taskRepository.getTask(id);

        assertNull(resultReal);
    }

    @Test
    public void deleteTask_Normal() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String text1 = "Hello";
        String text2 = "Hello 2";

        Task resultExpected1 = factory.getNewTask(id1, text1, TaskStatus.inbox);
        Task resultExpected2 = factory.getNewTask(id2, text2, TaskStatus.done);

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(anyString(), any(RowMapper.class), any())).
                thenReturn(resultExpected1, resultExpected2);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);
        Task resultReal1 = taskRepository.deleteTask(id1);
        Task resultReal2 = taskRepository.deleteTask(id2);

        resultExpected1 = factory.getNewTask(id1, text1, TaskStatus.inbox,
                resultExpected1.getCreatedAt(), resultReal1.getChangedAt());

        resultExpected2 = factory.getNewTask(id2, text2, TaskStatus.done,
                resultExpected2.getCreatedAt(), resultReal2.getChangedAt());

        assertEquals(resultExpected1, resultReal1);
        assertEquals(resultExpected2, resultReal2);
    }

    @Test
    public void updateTask_Normal() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).thenReturn(1,1, 0);

        String text = "Hello";
        String text1 = "Hello, world!";

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations, factory);

        Task originalTask = taskRepository.createTask(text);

        assertNotEquals(null, originalTask);

        Task resultExpected = factory.getNewTask(originalTask.getId(), text1,
                TaskStatus.done, originalTask.getCreatedAt());

        Task resultReal = taskRepository.updateTask(originalTask.getId(), resultExpected);

        assertNotEquals(null, resultReal);

        assertEquals(resultExpected, resultReal);
    }

    @Test
    public void updateTask_JDBCError() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).
                thenThrow(new RecoverableDataAccessException("Error!There is no 'dummy' row"));

        String id = UUID.randomUUID().toString();
        String text = "someTask";

        Task resultExpected = factory.getNewTask(id, text, TaskStatus.done);
        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations,  factory);
        assertNull(taskRepository.updateTask(id,resultExpected));
    }

}
