package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.core.model.Task;
import it.sevenbits.taskmanager.core.model.TaskFactory;
import it.sevenbits.taskmanager.core.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.UUID;

/**
 *Realisation of TaskRepository to work with database
 */

public class DatabaseTaskRepository implements TaskRepository {

    private JdbcOperations jdbcOperations;
    private TaskFactory taskFactory;
    private final Task emptyTask;
    private final Logger logger;

    private static final int TASK_ID = 1;
    private static final int TASK_TEXT = 2;
    private static final int TASK_STATUS = 3;
    private static final int TASK_CREATED_AT = 4;
    private static final int TASK_CHANGED_AT = 5;

    /**
     * Constructor for class
     * @param jdbcOperations JDBC object to interact with database
     */

    public DatabaseTaskRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        taskFactory = new TaskFactory();
        emptyTask = taskFactory.getNewTask("null", "emptyTask", TaskStatus.empty);
        logger = LoggerFactory.getLogger(DatabaseTaskRepository.class);
    }

    /**
     *Get List of Tasks from repository
     * @param status which status task need to be in list
     * @return List of Task Objects
     */

    public List<Task> getTaskList(final String status) {
        try {
            return jdbcOperations.query(
                    "SELECT id, name, status, createdAt, changedAt FROM task WHERE status = ?",
                    (resultSet, i) -> {
                        String resultId = resultSet.getString(TASK_ID);
                        String resultName = resultSet.getString(TASK_TEXT);
                        String resultStatus = resultSet.getString(TASK_STATUS);
                        String resultCreatedAt = resultSet.getString(TASK_CREATED_AT);
                        String resultChangedAt = resultSet.getString(TASK_CHANGED_AT);
                        return taskFactory.getNewTask(resultId, resultName, TaskStatus.resolveString(resultStatus),
                                resultCreatedAt, resultChangedAt);
                    },
                    status);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *Create new Task
     * @param text text of new Task
     * @return new Task Object or empty task if some error
     */

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
        } catch (Exception e) {
            logger.error(e.getMessage());
            return emptyTask;
        }
        return emptyTask;
    }

    /**
     * Get id for new Task
     * @return id
     */

    private UUID getNewId() {
        return UUID.randomUUID();
    }

    /**
     *Get task by id
     * @param id id of task to get
     * @return Task Object with that id, or empty task if error or task not found
     */

    public Task getTask(final String id) {
        try {
            return jdbcOperations.queryForObject(
                    "SELECT id, name, status, createdAt, changedAt FROM task WHERE id = ?",
                    (resultSet, i) -> {
                        String rowId = resultSet.getString(TASK_ID);
                        String rowName = resultSet.getString(TASK_TEXT);
                        TaskStatus rowStatus = TaskStatus.resolveString(resultSet.getString(TASK_STATUS));
                        String rowCreatedAt = resultSet.getString(TASK_CREATED_AT);
                        String rowChangedAt = resultSet.getString(TASK_CHANGED_AT);
                        if (rowStatus.is(TaskStatus.empty)) {
                            return emptyTask;
                        }
                        return taskFactory.getNewTask(rowId, rowName, rowStatus,
                                rowCreatedAt, rowChangedAt);
                    },
                    id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return emptyTask;
        }
    }

    /**
     *Delete task by id
     * @param id id of deleted task
     * @return Deleted task or empty task if some error
     */

    public Task deleteTask(final String id) {
        try {
            Task deletedTask1 = jdbcOperations.queryForObject(
                    "DELETE FROM task WHERE id = ? RETURNING id, name, status, createdAt, changedAt",
                    (resultSet, i) -> {
                        TaskStatus rowStatus = TaskStatus.resolveString(resultSet.getString(TASK_STATUS));
                        String rowId = resultSet.getString(TASK_ID);
                        String rowName = resultSet.getString(TASK_TEXT);
                        String rowCreatedAt = resultSet.getString(TASK_CREATED_AT);
                        String rowChangedAt = resultSet.getString(TASK_CHANGED_AT);
                        return taskFactory.getNewTask(rowId, rowName, rowStatus, rowCreatedAt, rowChangedAt);
                    },
                    id);
            if (!deletedTask1.getStatus().is(TaskStatus.empty)) {
                return deletedTask1;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return emptyTask;
    }

    /**
     *Update task by id with new version of it
     * @param id id of task to update
     * @param changedTask updated version of task
     * @return updated Task or empty task if error
     */
    public Task updateTask(final String id, final Task changedTask) {
        if (!changedTask.getStatus().is(TaskStatus.empty)) {
            try {
                int rowsInsert = jdbcOperations.update(
                        "INSERT INTO task (id, name, status, createdAt, changedAt) VALUES (?, ?, ?, ?, ?) " +
                                "ON CONFLICT(id) DO UPDATE SET name = ?, status = ?, changedAt = ?",
                        changedTask.getId(), changedTask.getText(), changedTask.getStatus().toString(),
                        changedTask.getCreatedAt(), changedTask.getChangedAt(), changedTask.getText(),
                        changedTask.getStatus().toString(), changedTask.getChangedAt()
                );
                if (rowsInsert > 0) {
                    return changedTask;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return emptyTask;
            }
        }
        return emptyTask;
    }
}
