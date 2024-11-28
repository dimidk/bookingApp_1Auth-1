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
    BookingLabService bookingLabService;
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

        return busers.getUsername();
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

    @GetMapping(value="/static_calendar")
    public String static_calendar() {
        return "static_calendar";
    }


    @GetMapping(value="/json_calendar")
    public String json_calendar(@RequestParam String username) {


        return "json_calendar";
    }
//
//    @GetMapping(value="/validate-user")
//    public String validation(HttpServletRequest request, HttpServletResponse response) {
//
//        String username = null;
//        log.info("I am in validate-user");
//
//        String token = jwtUtils.getJwtFromHeader(request);
//
////        log.info("validate-user: token: {} ",token);
//        if (token != null && jwtUtils.validateJwtToken(token) ) {
//            log.info("token is valid!");
//
//            username = jwtUtils.getUserNameFromJwtToken(token);
//
//
//            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
//            jwtCookie.setHttpOnly(true);  // Prevent JavaScript access
//            jwtCookie.setPath("/");
//            response.addCookie(jwtCookie);
//
//
//
//        }
//        log.info("username is " + username);
//
////
////            //store to session
////
////            response.sendRedirect("json_calendar");
////            return ResponseEntity.ok("{\"username\":\"" + username + "\"}");
////        }
//
//        return "json_calendar";
//
//    }

    @GetMapping(value="/jsonB_calendar")
    public String jsonB_calendar() {
        return "jsonB_calendar";
    }
}
