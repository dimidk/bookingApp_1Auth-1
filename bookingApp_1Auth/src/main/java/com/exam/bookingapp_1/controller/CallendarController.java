package com.exam.bookingapp_1.controller;

import com.exam.bookingapp_1.model.Busers;
import com.exam.bookingapp_1.service.BookingLabService;
import com.exam.bookingapp_1.service.BusersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class CallendarController {

    @Autowired
    private BusersService busersService;


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

//    @PostMapping(value="/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public String login( Busers busers) {
//
//        log.info("in post controller: {}",busers.getId());
//        return "redirect:/json_calendar";
//    }

    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }


}
