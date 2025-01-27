package com.exam.bookingapp_1.controller;

import com.exam.bookingapp_1.model.Busers;
import com.exam.bookingapp_1.model.OldBookingRequest;
import com.exam.bookingapp_1.repository.OldBookingRequestRepository;
import com.exam.bookingapp_1.service.BookingLabService;
import com.exam.bookingapp_1.service.BusersService;
import com.exam.bookingapp_1.service.OldBookingRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CallendarController {

    @Autowired
    private BusersService busersService;

    @Autowired
    private  OldBookingRequestService oldBookingRequestService;



    @RequestMapping ("/index")
    public String index() {

        return "index";
    }

    @RequestMapping("/register")
    public String register() {

        return "register";
    }

    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register( Busers busers) {

        log.info("register");

        busersService.save(busers);

       // return busers.getUsername();
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String login() {

        log.info("in get controller");
        return "login";
    }


    @RequestMapping("/registeredUsers")
    public String registeredUsers() {
        return "registeredUsers";
    }

    @PostMapping(value = "/registeredUsers",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registeredUsers(String username,String labname) {

        log.info("registeredUsers");

        oldBookingRequestService.add(OldBookingRequest.builder()
                .username(username)
                .labname(labname)
                .build());

        return "login";
    }

    @GetMapping ("/admin")
    public String showAdmin(Model model) {

        model.addAttribute("users",oldBookingRequestService.getAllUsernames());
        model.addAttribute("labs",oldBookingRequestService.getAllLabs());

        return "admin";
    }


    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }


}
