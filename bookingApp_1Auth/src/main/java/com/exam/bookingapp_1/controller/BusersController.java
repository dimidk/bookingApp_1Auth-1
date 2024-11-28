package com.exam.bookingapp_1.controller;

//import com.exam.bookingapp_1.auth.JwtUtils;
import com.exam.bookingapp_1.model.Busers;
import com.exam.bookingapp_1.repository.BusersRepository;
import com.exam.bookingapp_1.service.BusersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class BusersController {

    @Autowired
    private BusersRepository busersRepository;

    @Autowired
    private BusersService busersService;

//    @Autowired
//    private JwtUtils jwtUtils;

    @GetMapping("/user")
    public ResponseEntity<Map<String,String>> getAuthenticatedUser() {

        HashMap<String, String> authUser = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication != null && authentication.isAuthenticated()) {
            authUser.put("username", authentication.getName());
            authUser.put("role",authentication.getAuthorities().toString());
            return ResponseEntity.ok(authUser);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


//    @GetMapping("/validate-user")
//    public ResponseEntity<String> validateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        String username = null;
//
//        log.info("validate-user: try to validate user");
//
//        String token = jwtUtils.getJwtFromHeader(request);
//        log.info("validate-user: token: {} ",token);
//        if (token != null && jwtUtils.validateJwtToken(token) ) {
//            username = jwtUtils.getUserNameFromJwtToken(token);
//
//            //store to session
//
//            response.sendRedirect("json_calendar");
//            return ResponseEntity.ok("{\"username\":\"" + username + "\"}");
//        }
//
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
//    }


}
