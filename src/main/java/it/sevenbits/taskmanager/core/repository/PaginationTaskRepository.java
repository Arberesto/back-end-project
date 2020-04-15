package it.sevenbits.taskmanager.core.repository;

import it.sevenbits.taskmanager.web.model.GetTasksResponse;


public interface PaginationTaskRepository extends TaskRepository {

    GetTasksResponse getTaskList(final String status, final String order, final Integer page,
                                 final Integer size);
}
