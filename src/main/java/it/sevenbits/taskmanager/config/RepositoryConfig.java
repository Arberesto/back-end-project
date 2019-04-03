package it.sevenbits.taskmanager.config;


import it.sevenbits.taskmanager.core.repository.MapTaskRepository;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

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
    public TaskRepository taskRepository() {
        return new MapTaskRepository(new HashMap<>());
    }
}
