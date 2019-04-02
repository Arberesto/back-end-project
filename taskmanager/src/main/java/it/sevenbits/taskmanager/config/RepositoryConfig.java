package it.sevenbits.taskmanager.config;


import it.sevenbits.taskmanager.core.repository.SimpleTaskRepository;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new SimpleTaskRepository();
    }
}
