package it.sevenbits.taskmanager.core.service.task;

import it.sevenbits.taskmanager.core.model.task.Task;
import it.sevenbits.taskmanager.web.model.requests.PatchTaskRequest;

/**
 * Service class for serving Task
 */

public interface TaskService {

    /**
     * Update some task fields using TaskRequest model
     * @param task task to update
     * @param request some Model that contain fields
     * @return updated Task
     */

    Task update(Task task, PatchTaskRequest request);

}
