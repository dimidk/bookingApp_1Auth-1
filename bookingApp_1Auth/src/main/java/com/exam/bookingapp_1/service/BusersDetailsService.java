package com.exam.bookingapp_1.service;

import com.exam.bookingapp_1.model.Busers;
import com.exam.bookingapp_1.repository.BusersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusersDetailsService implements UserDetailsService {

    @Autowired
    private BusersRepository busersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Busers> busers = busersRepository.findBusersByUsername(username);
        if (busers.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        Busers buser = busers.get();

//        Set<GrantedAuthority> authorities = buser.getRoles().stream()
//                .map((role) -> new SimpleGrantedAuthority(role.getRolename()))
//                .collect(Collectors.toSet());



        UserDetails userDetails = User.builder()
                .username(buser.getUsername())
                .password(buser.getPassword())
                .authorities(buser.getAuthorities())
                .build();
        return userDetails;
    }
}
