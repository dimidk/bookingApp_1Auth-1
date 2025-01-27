package com.exam.bookingapp_1.controller;

import com.exam.bookingapp_1.model.BookingLab;
import com.exam.bookingapp_1.service.AdminService;
import com.exam.bookingapp_1.service.BookingLabService;
import com.exam.bookingapp_1.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final BookingLabService bookingLabService;
//    private final BookingService bookingService;
    private final AdminService adminService;


    @PostMapping("/loadLastYear")
    public ResponseEntity<HttpStatus> loadLastYear(@RequestBody String bodyinfo) {

        List<BookingLab> bookings;

        String infoDecod = URLDecoder.decode(bodyinfo, StandardCharsets.UTF_8);
//        log.info("request body decoded {}", infoDecod);

        String username = infoDecod.split("&")[0].split("=")[1];
        String labname = infoDecod.split("&")[1].split("=")[1];
        String startSemester = infoDecod.split("&")[2].split("=")[1];
        LocalDate startDateSemester = LocalDate.parse(startSemester);
        log.info("In the administration controller with parameters {} {} {}", username, labname, startSemester);

        bookings = bookingLabService.getBookingsByLabnameAndUsername(username, labname);
        if (bookings.isEmpty()) {
            log.info("No bookings found for username {}", username);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        adminService.createSemesterBooking(bookings, startDateSemester);

        return ResponseEntity.status(HttpStatus.OK).build();

    }

    public ResponseEntity<HttpStatus> exportDatabase() {

        log.info("In the administration controller");

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
