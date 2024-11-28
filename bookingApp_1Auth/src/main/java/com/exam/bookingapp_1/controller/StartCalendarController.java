package com.exam.bookingapp_1.controller;


import com.exam.bookingapp_1.model.BookingLab;
import com.exam.bookingapp_1.service.BookingLabService;
import com.exam.bookingapp_1.service.BusersDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller()
@RequiredArgsConstructor
@Slf4j
public class StartCalendarController {

//    private final AuthenticationManager authenticationManager;
//    private final BusersDetailsService busersDetailsService;
    @Autowired
    private BookingLabService bookingLabService;



    @RequestMapping(value = "/displayBookingsA", method = RequestMethod.GET)
    public ResponseEntity<List<BookingLab>> getAllBookingsA() throws IOException {

        List<BookingLab> allBookings = new ArrayList<>();
        String labname = "ΔΕΠΥ Α";

        allBookings = bookingLabService.getByLabname(labname);

        return ResponseEntity.ok(allBookings);

    }

    @RequestMapping(value = "/displayBookingsB", method = RequestMethod.GET)
    public ResponseEntity<List<BookingLab>> getAllBookingsB() throws IOException {

        List<BookingLab> allBookings = new ArrayList<>();
        String labname = "ΔΕΠΥ Β";

        allBookings = bookingLabService.getByLabname(labname);

        return ResponseEntity.ok(allBookings);

    }


    @RequestMapping(value = "/displayBookingsC", method = RequestMethod.GET)
    public ResponseEntity<List<BookingLab>> getAllBookingsC() throws IOException {

        List<BookingLab> allBookings = new ArrayList<>();
        String labname = "ΔΕΠΥ Γ";

        allBookings = bookingLabService.getByLabname(labname);

        return ResponseEntity.ok(allBookings);

    }


    @RequestMapping("/depya")
    public String depya() {
        return "depya";
    }

    @RequestMapping("/depyb")
    public String depyb() {
        return "depyb";
    }

    @RequestMapping("/depyc")
    public String depyc() {
        return "depyc";
    }
}
