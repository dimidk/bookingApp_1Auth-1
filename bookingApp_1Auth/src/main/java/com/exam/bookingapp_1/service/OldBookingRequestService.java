package com.exam.bookingapp_1.service;

import com.exam.bookingapp_1.model.OldBookingRequest;
import com.exam.bookingapp_1.repository.OldBookingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for old booking requests. This is for users who asked to keep to
 * new semester previous bookings
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class OldBookingRequestService {


    private final OldBookingRequestRepository oldBookingRequestRep;

    public void add(OldBookingRequest oldBookingRequest) { oldBookingRequestRep.save(oldBookingRequest);}

    public List<String> getAllUsernames() {

        List<OldBookingRequest> oldBookingRequests = oldBookingRequestRep.findAll();

        oldBookingRequests.forEach(oldBookingRequest -> {log.info(oldBookingRequest.getUsername());});

        return oldBookingRequests.stream().map(OldBookingRequest::getUsername).toList();
    }

    public List<String> getAllLabs() {

        List<OldBookingRequest> oldBookingRequests = oldBookingRequestRep.findAll();

        oldBookingRequests.forEach(oldBookingRequest -> {log.info(oldBookingRequest.getLabname());});

        return oldBookingRequests.stream().map(OldBookingRequest::getLabname).toList();

    }
}
