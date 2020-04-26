package it.sevenbits.taskmanager.config;

import it.sevenbits.taskmanager.core.model.task.TaskFactory;
import it.sevenbits.taskmanager.core.model.user.UserFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *Config to get TaskFactory Singleton
 */

@Configuration
public class FactoryConfig {

    /**
     * Bean to get TaskFactory
     * @return new TaskFactory Object
     */

    @Bean
    @Qualifier("taskFactory")
    public TaskFactory taskFactory() {
        return new TaskFactory();
    }

    /**
     * Bean to get UserFactory
     * @return new UserFactory Object
     */

    @Bean
    @Qualifier("userFactory")
    public UserFactory userFactory() {
        return new UserFactory();
    }
}
