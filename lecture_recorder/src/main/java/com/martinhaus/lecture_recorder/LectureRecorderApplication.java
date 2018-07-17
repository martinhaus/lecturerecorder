package com.martinhaus.lecture_recorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LectureRecorderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LectureRecorderApplication.class, args);
    }
}
