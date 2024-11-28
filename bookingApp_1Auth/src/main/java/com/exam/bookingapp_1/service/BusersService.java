package com.exam.bookingapp_1.service;

import com.exam.bookingapp_1.model.Brole;
import com.exam.bookingapp_1.model.Busers;
import com.exam.bookingapp_1.model.Role;
import com.exam.bookingapp_1.repository.BusersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
//public class BusersService implements UserDetailsService {
public class BusersService {

    @Autowired
    BusersRepository busersRepository;

    //@Override
    public Busers loadUser(String username) throws  UserPrincipalNotFoundException {

        Optional<Busers> user = busersRepository.findBusersByUsername(username);
//                .orElseThrow(() -> new UsernameNotFoundException("There is no such a user")));
//        Busers test = Busers.builder().bname(username).build();
//        boolean userf = busersRepository.existsBusersByBname(username);

        if (Optional.ofNullable(user).isEmpty()) {
            throw new UserPrincipalNotFoundException("User not found");

        }
        log.info("this is the username exists: {}", username);
//      user.get().getRoles().stream().map(Brole::getBrolename).collect(Collectors.toSet())
        log.info("this is the user object: name: {},password: {}, role: {}",user.get().getUsername(),user.get().getPassword());

//        if (user.isEmpty()) {
//            throw new UsernameNotFoundException("User not found");
//        }
        Busers busers = user.get();
       // List<String> roles = busers.getRoles().stream().collect(Brole::getRolename).toString();
//
//        Set<GrantedAuthority> authorities = busers.getRoles().stream()
//                .map((role) -> new SimpleGrantedAuthority(role.getRolename()))
//                .collect(Collectors.toSet());



//        return new org.springframework.security.core.userdetails.User(
//                busers.getUsername(),
//                busers.getPassword(),
//                authorities
//        );

        return busers;
    }

    public void save(Busers busers) {

        String username = busers.getUsername();

        Optional<Busers> userFound= busersRepository.findBusersByUsername(username);
        if (userFound.isPresent()) {
            throw new RuntimeException("User already exists");
        }


        Busers newUser = Busers.builder()
                .username(username)
                .password(busers.getPassword())
                .role(Role.USER)
                .build();

        busersRepository.save(newUser);
    }
}
