package it.sevenbits.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Start point of application
 */

@SpringBootApplication
public final class Main {

    private Main(){}

    /**
     * Start point of application
     * @param args arguments of command line
     */

    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }
}