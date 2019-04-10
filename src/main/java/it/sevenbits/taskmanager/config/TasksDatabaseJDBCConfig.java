package it.sevenbits.taskmanager.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class TasksDatabaseJDBCConfig {
    @Bean
    @FlywayDataSource
    @Qualifier("tasksJdbcOperations")
    @ConfigurationProperties(prefix = "spring.datasource.tasks")
    public JdbcOperations tasksJdbcOperations(
            @Qualifier("tasksDataSource")
                    DataSource tasksDataSource
    ) {
        return new JdbcTemplate(tasksDataSource);
    }
}
