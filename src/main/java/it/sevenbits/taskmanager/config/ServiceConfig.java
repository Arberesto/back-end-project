package it.sevenbits.taskmanager.config;

import it.sevenbits.taskmanager.core.model.task.TaskFactory;
import it.sevenbits.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.taskmanager.core.service.task.SimpleTaskService;
import it.sevenbits.taskmanager.core.service.task.TaskService;
import it.sevenbits.taskmanager.core.service.user.UserService;
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
