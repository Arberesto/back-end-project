package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.UUID;

public class DatabaseTaskRepository implements TaskRepository {

    private JdbcOperations jdbcOperations;
    private TaskFactory taskFactory;
    private final Task emptyTask;

    public DatabaseTaskRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        taskFactory = new TaskFactory();
        emptyTask = taskFactory.getNewTask("null", "emptyTask", TaskStatus.empty);
    }
    public List<Task> getTaskList(final String status) {
        return jdbcOperations. query(
                "SELECT id, name, status FROM task",
                (resultSet, i) -> {
                    String resultId = resultSet.getString(1);
                    String resultName = resultSet.getString(2);
                    String resultStatus = resultSet.getString(3);
                    return taskFactory.getNewTask(resultId, resultName, TaskStatus.resolveString(resultStatus));
                });
    }

    public Task createTask(final String text) {
        if ("".equals(text) || "".equals(text.trim())) {
            return emptyTask;
        }
        UUID id = getNewId();
        TaskStatus status = TaskStatus.inbox;
        Task result = taskFactory.getNewTask(id.toString(), text, status);
        try {
            int rows = jdbcOperations.update(
                    "INSERT INTO task (id, name, status) VALUES (?, ?, ?)",
                    id, text, status
            );
            if (rows == 0) {
                return emptyTask;
            }
        } catch (DataAccessException e) {
            return emptyTask;
        }
        return result;
    }

    /**
     * Get id for new Task
     * @return id
     */

    private UUID getNewId()
    {
        return jdbcOperations.queryForObject(
                "select nextval('taskt nextval(_id_seq')", UUID.class);
    }

    public Task getTask(String id) {
        return jdbcOperations.queryForObject(
                "SELECT id, name, status FROM task WHERE id = ?",
                (resultSet, i) -> {
                    UUID rowId = resultSet.getObject(1, UUID.class);
                    String rowName = resultSet.getString(2);
                    TaskStatus rowStatus = TaskStatus.resolveString(resultSet.getString(3));
                    if (rowStatus == TaskStatus.empty) {
                        return emptyTask;
                    }
                    return taskFactory.getNewTask(rowId.toString(), rowName, rowStatus);
                },
                id);
    }

    public Task deleteTask(final String id) {
        return emptyTask;
    }

    public Task updateTask(final String id, final Task changedTask) {
        return emptyTask;
    }
}
