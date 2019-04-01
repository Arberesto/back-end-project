package it.sevenbits.taskmanager.config;


import it.sevenbits.taskmanager.core.repository.SimpleTaskRepository;
import it.sevenbits.taskmanager.core.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public TaskRepository TaskRepository() {
        return new SimpleTaskRepository();
    }
}
