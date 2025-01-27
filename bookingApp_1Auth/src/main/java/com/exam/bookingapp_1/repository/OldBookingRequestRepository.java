package com.exam.bookingapp_1.repository;

import com.exam.bookingapp_1.model.OldBookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OldBookingRequestRepository extends JpaRepository<OldBookingRequest, Integer> {

    List<OldBookingRequest> findAll();

    List<OldBookingRequestRepository> findByUsername(String username);
    List<OldBookingRequestRepository> findByLabname(String labname);

}
