package com.exam.bookingapp_1.repository;

import com.exam.bookingapp_1.model.Busers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusersRepository extends JpaRepository<Busers, Integer> {


    Optional<Busers> findBusersByUsername(String username);


}

