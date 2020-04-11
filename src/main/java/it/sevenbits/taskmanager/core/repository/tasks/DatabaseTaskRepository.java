package it.sevenbits.taskmanager.core.repository.tasks;

import it.sevenbits.taskmanager.core.model.task.Task;
import it.sevenbits.taskmanager.core.model.task.TaskFactory;
import it.sevenbits.taskmanager.core.model.task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.UUID;

/**
 *Realisation of TaskRepository to work with database
 */

public class DatabaseTaskRepository implements PaginationTaskRepository {

    private JdbcOperations jdbcOperations;
    private TaskFactory taskFactory;
    private final Logger logger;

    private final String taskId = "id";
    private final String taskText = "name";
    private final String taskStatus = "status";
    private final String taskCreatedAt = "createdAt";
    private final String taskUpdatedAt = "changedAt";

    /**
     * Constructor for class
     * @param jdbcOperations JDBC object to interact with database
     * @param factory Taskfactory to create new Task objects
     */

    public DatabaseTaskRepository(final JdbcOperations jdbcOperations, final TaskFactory factory) {
        this.jdbcOperations = jdbcOperations;
        taskFactory = factory;
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
                        String resultId = resultSet.getString(taskId);
                        String resultName = resultSet.getString(taskText);
                        String resultStatus = resultSet.getString(taskStatus);
                        String resultCreatedAt = resultSet.getString(taskCreatedAt);
                        String resultChangedAt = resultSet.getString(taskUpdatedAt);
                        return taskFactory.getNewTask(
                                resultId, resultName, TaskStatus.resolveString(resultStatus),
                                resultCreatedAt, resultChangedAt);
                    },
                    status);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *Get List of Tasks from repository with pagination
     * @param status which status task need to be in list
     * @param order in which order tasks will be sorted
     * @param page which page to get
     * @param size how many objects in one page
     * @return List of Task objects of current page
     */

    public List<Task> getTaskList(final String status, final String order, final Integer page,
                                  final Integer size) {
        try {
            return jdbcOperations.query(
                    "SELECT id, name, status, createdAt, changedAt " +
                            "FROM task WHERE status = ? ORDER by createdAt DESC LIMIT ? OFFSET ?",
                    (resultSet, i) -> {
                        String resultId = resultSet.getString(taskId);
                        String resultName = resultSet.getString(taskText);
                        String resultStatus = resultSet.getString(taskStatus);
                        String resultCreatedAt = resultSet.getString(taskCreatedAt);
                        String resultUpdatedAt = resultSet.getString(taskUpdatedAt);
                        return taskFactory.getNewTask(
                                resultId, resultName, TaskStatus.resolveString(resultStatus),
                                        resultCreatedAt, resultUpdatedAt);
                    },
                    status, size, size * (page - 1));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     *Create new Task
     * @param text text of new Task
     * @return new Task Object or empty task if some error
     */

    public Task createTask(final String text) {
        if (text == null || "".equals(text.trim())) {
            logger.warn("text of task to create shouldn't be empty");
            return null;
        }
        String id = getNewId().toString();
        TaskStatus status = TaskStatus.inbox;
        Task result = taskFactory.getNewTask(id, text, status);
        try {
            int rows = jdbcOperations.update(
                    "INSERT INTO task (id, name, status, createdAt, changedAt) VALUES (?, ?, ?, ?, ?)",
                    id, text, status.toString(), result.getCreatedAt(), result.getUpdatedAt()
            );
            if (rows > 0) {
                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return null;
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
                        String rowId = resultSet.getString(taskId);
                        String rowName = resultSet.getString(taskText);
                        TaskStatus rowStatus = TaskStatus.resolveString(resultSet.getString(taskStatus));
                        String rowCreatedAt = resultSet.getString(taskCreatedAt);
                        String rowChangedAt = resultSet.getString(taskUpdatedAt);
                        if (rowStatus == null) {
                            return null;
                        }
                        return taskFactory.getNewTask(rowId, rowName, rowStatus,
                                rowCreatedAt, rowChangedAt);
                    },
                    id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     *Delete task by id
     * @param id id of deleted task
     * @return Deleted task or empty task if some error
     */

    public Task deleteTask(final String id) {
        try {
            Task deletedTask = jdbcOperations.queryForObject(
                    "DELETE FROM task WHERE id = ? RETURNING id, name, status, createdAt, changedAt",
                    (resultSet, i) -> {
                        TaskStatus rowStatus = TaskStatus.resolveString(resultSet.getString(taskStatus));
                        String rowId = resultSet.getString(taskId);
                        String rowName = resultSet.getString(taskText);
                        String rowCreatedAt = resultSet.getString(taskCreatedAt);
                        String rowChangedAt = resultSet.getString(taskUpdatedAt);
                        return taskFactory.getNewTask(
                                rowId, rowName, rowStatus, rowCreatedAt, rowChangedAt);
                    },
                    id);
            if (deletedTask != null) {
                return deletedTask;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     *Update task by id with new version of it
     * @param id id of task to update
     * @param changedTask updated version of task
     * @return updated Task or empty task if error
     */
    public Task updateTask(final String id, final Task changedTask) {
        if (changedTask != null) {
            try {
                int rowsInsert = jdbcOperations.update(
                        "INSERT INTO task (id, name, status, createdAt, changedAt) VALUES (?, ?, ?, ?, ?) " +
                                "ON CONFLICT(id) DO UPDATE SET name = ?, status = ?, changedAt = ?",
                        changedTask.getId(), changedTask.getText(), changedTask.getStatus().toString(),
                        changedTask.getCreatedAt(), changedTask.getUpdatedAt(), changedTask.getText(),
                        changedTask.getStatus().toString(), changedTask.getUpdatedAt()
                );
                if (rowsInsert > 0) {
                    return changedTask;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        return null;
    }

}
