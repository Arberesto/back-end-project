package it.sevenbits.taskmanager.config;


import it.sevenbits.taskmanager.core.model.task.TaskFactory;
import it.sevenbits.taskmanager.core.model.user.UserFactory;
import it.sevenbits.taskmanager.core.repository.tasks.DatabaseTaskRepository;
import it.sevenbits.taskmanager.core.repository.tasks.PaginationTaskRepository;

import it.sevenbits.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.taskmanager.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Config for Repository default objects
 */

@Configuration
public class RepositoryConfig {



    /**
     * Bean to get TaskRepository Object
     * @param jdbcOperations JDBC object to interact with database
     * @param taskFactory TaskFactory to create new Task objects
     * @return TaskRepository Object
     */

    @Bean
    @Qualifier("taskRepository")
    public PaginationTaskRepository taskRepository(
            @Qualifier("tasksJdbcOperations") final JdbcOperations jdbcOperations,
            @Qualifier("taskFactory") final TaskFactory taskFactory) {
        return new DatabaseTaskRepository(jdbcOperations, taskFactory);
    }

    /**
     * Bean to get UsersRepository Object
     * @param jdbcOperations JDBC to interact with database
     * @param passwordEncoder PasswordEncoder to work with passwords
     * @param userFactory factory to create User objects
     * @return UsersRepository Object
     */

    @Bean
    @Qualifier("UsersRepository")
    public UsersRepository usersRepository(
            @Qualifier("tasksJdbcOperations") final JdbcOperations jdbcOperations,
            @Qualifier("passwordEncoder") final PasswordEncoder passwordEncoder,
            @Qualifier("userFactory") final UserFactory userFactory) {
        return new UsersRepository(jdbcOperations, passwordEncoder, userFactory);
    }
}
