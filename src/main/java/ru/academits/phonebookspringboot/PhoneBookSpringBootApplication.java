package ru.academits.phonebookspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhoneBookSpringBootApplication {
    static void main(String[] args) {
        SpringApplication.run(PhoneBookSpringBootApplication.class, args);
    }
}