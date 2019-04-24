package it.sevenbits.taskmanager.config;


import it.sevenbits.taskmanager.core.repository.DatabaseTaskRepository;
import it.sevenbits.taskmanager.core.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;


/**
 * Config for TaskRepository default object
 */

@Configuration
public class RepositoryConfig {



    /**
     * Get TaskRepository Object when controller need it
     * @return TaskRepository Object
     */

    @Bean
    public TaskRepository taskRepository(
            @Qualifier("tasksJdbcOperations") JdbcOperations jdbcOperations) {
        return new DatabaseTaskRepository(jdbcOperations);
    }
}
