package it.sevenbits.taskmanager.core.repository.tasks;

import it.sevenbits.taskmanager.core.model.task.Task;

import java.util.List;

public interface PaginationTaskRepository extends TaskRepository {

    List<Task> getTaskList(final String status, final String order, final Integer page,
                           final Integer size);
}
