package it.sevenbits.taskmanager.core.repository;
import it.sevenbits.taskmanager.core.model.SimpleTask;
import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskRepositoryTest {

    //getTaskList - jdbcOperations.query
    @Test
    public void getTaskListTest1() {
        List<Task> result1 = new ArrayList<>();

        List<Task> result2 = new ArrayList<>();
        result2.add(new SimpleTask(UUID.randomUUID().toString(),"firstTask", TaskStatus.done));
        result2.add(new SimpleTask(UUID.randomUUID().toString(),"secondTask", TaskStatus.done));

        List<Task> result3 = new ArrayList<>();
        result3.add(new SimpleTask(UUID.randomUUID().toString(),"thirdTask", TaskStatus.inbox));

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.query(anyString(), any(RowMapper.class),any())).
                thenReturn(result1, result2, result3);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);

        assertEquals("Not equal1?!",result1,taskRepository.getTaskList(TaskStatus.done.toString()));

        assertEquals("Not equal2?!",result2,taskRepository.getTaskList(TaskStatus.done.toString()));

        assertEquals("Not equal3?!",result3,taskRepository.getTaskList(TaskStatus.inbox.toString()));



    }

    /**
     *Situation when there is exception in jdbc
     */

    @Test
    public void getTaskListTest2() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.query(anyString(), any(RowMapper.class),any())).
                thenThrow(new RecoverableDataAccessException("Error!There is no 'dummy' row"));
        // there should be DataAccessException, but it's abstract, so I use it's subclass

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);
        try {
            taskRepository.getTaskList(TaskStatus.inbox.toString());
        } catch (DataAccessException e) {
            assertEquals(e.getMessage(), "Error!There is no 'dummy' row");
        }
    }

    @Test
    public void createTaskTest1() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).thenReturn(1, 1, 0);

        String id = UUID.randomUUID().toString();
        String text = "Hello";

        Task resultExpected = new SimpleTask(id,text);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);

        Task resultReal = taskRepository.createTask(text);
        resultExpected.setId(resultReal.getId());

        assertTrue(resultExpected.equals(resultReal));
    }
    //createTask - jdbcOperations.update

    //getTask - jdbcOperations.queryForObject

    //deleteTask - jdbcOperations.queryForObject

    @Test
    public void updateTaskTest1() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(anyString(),any(Object.class))).thenReturn(1, 1, 0);

        String id = UUID.randomUUID().toString();
        String text = "Hello";

        Task resultExpected = new SimpleTask(id,text);

        TaskRepository taskRepository = new DatabaseTaskRepository(jdbcOperations);

        Task resultReal = taskRepository.updateTask(id,resultExpected);
        resultExpected.setId(resultReal.getId());

        assertTrue(resultExpected.equals(resultReal));
    }

    //updateTask - jdbcOperations.update
}
