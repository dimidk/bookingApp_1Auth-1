package com.exam.bookingapp_1.config;

import com.exam.bookingapp_1.repository.BusersRepository;
import com.exam.bookingapp_1.service.BusersDetailsService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {


    private final BusersRepository busersRepository;
    private final BusersDetailsService busersDetailsService;

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        return username ->
//                (org.springframework.security.core.userdetails.UserDetails) busersRepository.findBusersByUsername(username)
//                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return  NoOpPasswordEncoder.getInstance();
    }

    @Bean(name="authProvider")
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(busersDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());


        return daoAuthenticationProvider;
    }

   @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {


        return config.getAuthenticationManager();
    }


//    @Bean
//    public Properties mailProperties() {
//
//        Properties props = System.getProperties();
//        String smtpHost = "smtp.ntua.gr";
//        props.put("mail.smtp.host", smtpHost);
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props, null);
//
//        return props;
//    }


    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("dimideka.dimi@gmail.com");
        mailSender.setPassword("dpik izbg jekt wjkb");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }



}
