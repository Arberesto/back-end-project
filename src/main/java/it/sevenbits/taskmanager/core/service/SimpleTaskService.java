package it.sevenbits.taskmanager.core.service;

import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.core.model.Task.TaskFactory;
import it.sevenbits.taskmanager.core.model.Task.TaskStatus;
import it.sevenbits.taskmanager.web.model.PatchTaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for serving DataRepositoryTask
 */

@Service
public class SimpleTaskService implements TaskService {

    private TaskFactory factory;
    private Logger logger;

    /**
     * Public constructor
     * @param factory TaskFactory to create new Task Objects
     */

    public SimpleTaskService(final TaskFactory factory) {

        this.factory = factory;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Update some task fields using PatchTaskRequest model
     * @param task task to update
     * @param request Model that contain text and status
     * @return updated Task
     */

    public Task update(final Task task, final PatchTaskRequest request) {
        try {
            if (request.getText() != null &&
                    (TaskStatus.resolveString(request.getStatus()) != null)) {
                return factory.getNewTask(task.getId(), request.getText(),
                        TaskStatus.resolveString(request.getStatus()), task.getCreatedAt());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
