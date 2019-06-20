package it.sevenbits.taskmanager.config;

import it.sevenbits.taskmanager.core.model.Task.TaskFactory;
import it.sevenbits.taskmanager.core.service.SimpleTaskService;
import it.sevenbits.taskmanager.core.service.TaskService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Config to create TaskService Object
 */

@Configuration
public class ServiceConfig {

    /**
     * Bean to get TaskService
     * @param factory TaskFactory Object
     * @return new TaskService Object
     */

    @Bean
    @Qualifier("taskService")
    public TaskService taskService(@Qualifier("taskFactory") final TaskFactory factory) {
        return new SimpleTaskService(factory);
    }
}
