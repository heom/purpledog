package com.example.purpledog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PurpledogApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurpledogApplication.class, args);
    }

}
