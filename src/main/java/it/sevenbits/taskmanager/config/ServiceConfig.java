package it.sevenbits.taskmanager.config;

import it.sevenbits.taskmanager.core.model.TaskFactory.TaskFactory;
import it.sevenbits.taskmanager.core.service.SimpleTaskService;
import it.sevenbits.taskmanager.core.service.TaskService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    public TaskService taskService(@Qualifier("taskFactory") final TaskFactory factory) {
        return new SimpleTaskService(factory);
    }
}
