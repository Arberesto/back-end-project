package it.sevenbits.taskmanager.config;

import it.sevenbits.taskmanager.core.model.Task.TaskFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *Config to get TaskFactory Singleton
 */

@Configuration
public class TaskFactoryConfig {

    /**
     * Bean to get TaskFactory
     * @return new TaskFactory Object
     */

    @Bean
    @Qualifier("taskFactory")
    public TaskFactory taskFactory() {
        return new TaskFactory();
    }
}
