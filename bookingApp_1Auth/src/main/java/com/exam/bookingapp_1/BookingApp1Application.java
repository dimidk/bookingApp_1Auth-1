package com.exam.bookingapp_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookingApp1Application {

    public static void main(String[] args) {
        SpringApplication.run(BookingApp1Application.class, args);
    }

}
