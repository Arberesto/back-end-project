package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        return jdbcOperations.query(
                "SELECT id, name, status, createdAt, changedAt FROM task WHERE status = ?",
                (resultSet, i) -> {
                    String resultId = resultSet.getString(1);
                    String resultName = resultSet.getString(2);
                    String resultStatus = resultSet.getString(3);
                    String resultCreatedAt = resultSet.getString(4);
                    String resultChangedAt = resultSet.getString(5);
                    return taskFactory.getNewTask(resultId, resultName, TaskStatus.resolveString(resultStatus),
                            resultCreatedAt, resultChangedAt);
                },
                status);
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
                    "INSERT INTO task (id, name, status, createdAt, changedAt) VALUES (?, ?, ?, ?, ?)",
                    id, text, status.toString(), result.getCreatedAt(), result.getChangedAt()
            );
            if (rows > 0) {
                return result;
            }
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            return emptyTask;
        }
        return emptyTask;
    }

    /**
     * Get id for new Task
     *
     * @return id
     */

    private UUID getNewId() {
        return UUID.randomUUID();
    }

    public Task getTask(String id) {
        try {
            return jdbcOperations.queryForObject(
                    "SELECT id, name, status, createdAt, changedAt FROM task WHERE id = ?",
                    (resultSet, i) -> {
                        String rowId = resultSet.getString(1);
                        String rowName = resultSet.getString(2);
                        TaskStatus rowStatus = TaskStatus.resolveString(resultSet.getString(3));
                        String rowCreatedAt = resultSet.getString(4);
                        String rowChangedAt = resultSet.getString(5);
                        if (rowStatus == TaskStatus.empty) {
                            return emptyTask;
                        }
                        return taskFactory.getNewTask(rowId, rowName, rowStatus,
                                rowCreatedAt, rowChangedAt);
                    },
                    id);
        } catch (EmptyResultDataAccessException e) {
            logger.error(e.getMessage());
            return emptyTask;
        }
    }

    public Task deleteTask(final String id) {
        Task deletedTask;
        try {
            deletedTask = jdbcOperations.queryForObject(
                    "SELECT id, name, status, createdAt FROM task WHERE id = ?",
                    (resultSet, i) -> {
                        TaskStatus rowStatus = TaskStatus.resolveString(resultSet.getString(3));
                        if (rowStatus == TaskStatus.empty) {
                            return emptyTask;
                        }
                        String rowId = resultSet.getString(1);
                        String rowName = resultSet.getString(2);
                        String rowCreatedAt = resultSet.getString(4);
                        return taskFactory.getNewTask(rowId, rowName, rowStatus, rowCreatedAt);
                    },
                    id);

        } catch (EmptyResultDataAccessException e) {
            logger.error(e.getMessage());
            return emptyTask;
        }
        if (deletedTask.getStatus() != TaskStatus.empty) {
            int rows = jdbcOperations.update(
                    "DELETE FROM task WHERE id = ?",
                    id);
            if (rows > 0) {
                return deletedTask;
            }
        }
        return emptyTask;
    }

    public Task updateTask(final String id, final Task changedTask) {
        if (changedTask.getStatus() != TaskStatus.empty) {
            int rows = jdbcOperations.update(
                    "DELETE FROM task WHERE id = ?",
                    id);
            if (rows > 0) {
                try {
                    int rowsInsert = jdbcOperations.update(
                            "INSERT INTO task (id, name, status, createdAt, changedAt) VALUES (?, ?, ?, ?, ?)",
                            changedTask.getId(), changedTask.getText(), changedTask.getStatus().toString(),
                            changedTask.getCreatedAt(), changedTask.getChangedAt()
                    );
                    if (rowsInsert > 0) {
                        return changedTask;
                    }
                } catch (DataAccessException e) {
                    logger.error(e.getMessage());
                    return emptyTask;
                }
            }
        }
        return emptyTask;
    }
}
