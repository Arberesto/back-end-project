package it.sevenbits.taskmanager.core.repository.tasks;

import it.sevenbits.taskmanager.web.model.responce.GetTasksResponse;

public interface PaginationTaskRepository extends TaskRepository {

    /**
     * Get task list with page distribution
     * @param status which status task need to be in list
     * @param order in which order tasks will be sorted
     * @param page which page to get
     * @param size how many objects in one page
     * @return List of Task objects of current page
     */

    GetTasksResponse getTaskList(String status, String order, Integer page,
                                 Integer size);
}
