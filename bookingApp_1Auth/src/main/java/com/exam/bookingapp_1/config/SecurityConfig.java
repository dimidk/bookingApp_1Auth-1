package com.exam.bookingapp_1.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final MyAuthenticationHandler myAuthenticationHandler;


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/").permitAll()
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/logout").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/registeredUsers").permitAll()
                                .requestMatchers("/admin").hasAuthority("ADMIN")
                               // .requestMatchers("/depya").hasRole("USER")
                                .anyRequest().authenticated());
                http.formLogin(form -> form.loginPage("/login").permitAll()
                                .successHandler(myAuthenticationHandler))
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"));

        return http.build();

    }

}
