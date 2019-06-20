package it.sevenbits.taskmanager.core.service;

import it.sevenbits.taskmanager.core.model.Task.Task;
import it.sevenbits.taskmanager.web.model.PatchTaskRequest;

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
