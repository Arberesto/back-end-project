package it.sevenbits.taskmanager.config;

import it.sevenbits.taskmanager.core.model.TaskFactory.TaskFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */

@Configuration
public class TaskFactoryConfig {

    @Bean
    @Qualifier("taskFactory")
    public TaskFactory taskFactory() {
        return new TaskFactory();
    }
}
