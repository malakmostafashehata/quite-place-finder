package org.example.quiet_place;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync
@SpringBootApplication

public class QuietPlaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuietPlaceApplication.class, args);
    }

}
