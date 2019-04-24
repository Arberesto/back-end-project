package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.UUID;

public class DatabaseTaskRepository implements TaskRepository {

    private JdbcOperations jdbcOperations;
    private TaskFactory taskFactory;
    private final Task emptyTask;
    private final Logger logger;

    public DatabaseTaskRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        taskFactory = new TaskFactory();
        emptyTask = taskFactory.getNewTask("null", "emptyTask", TaskStatus.empty);
        logger = LoggerFactory.getLogger(DatabaseTaskRepository.class);
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
            logger.warn("text of task to create is empty");
            return emptyTask;
        }
        String id = getNewId().toString();
        TaskStatus status = TaskStatus.inbox;
        Task result = taskFactory.getNewTask(id, text, status);
        try {
            int rows = jdbcOperations.update(
                    "INSERT INTO task (id, name, status) VALUES (?, ?, ?)",
                    id, text, status.toString()
            );
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
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
        return UUID.randomUUID();
    }

    public Task getTask(String id) {
        return jdbcOperations.queryForObject(
                "SELECT id, name, status FROM task WHERE id = ?",
                (resultSet, i) -> {
                    UUID rowId = UUID.fromString(resultSet.getString(1));
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
