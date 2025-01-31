package com.exam.bookingapp_1.service;

import com.exam.bookingapp_1.model.ReservedSlots;
import com.exam.bookingapp_1.repository.ReservedSlotsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/***
 * Service responsible for managing reserved Time Slots
 ***/

@Service
@AllArgsConstructor
@Slf4j
public class ReservedSlotsService {

    private final ReservedSlotsRepository reservedSlotsRepository;

    public void add(ReservedSlots reservedSlots) {
        reservedSlotsRepository.save(reservedSlots);
    }

    public void addAll(List<ReservedSlots> reservedSlotsList) {
        reservedSlotsRepository.saveAll(reservedSlotsList);
    }

    public List<ReservedSlots> getAllSlots() {
        return reservedSlotsRepository.findAll();
    }

    public List<ReservedSlots> getReservedSlotsBetween (String labname, LocalDateTime start,LocalDateTime end) {

        log.info("Checking for reserved dates:{} {}", start, end);

        //return reservedSlotsRepository.getReservedSlotsByStartBetween(start, end);
        return reservedSlotsRepository.getReservedSlotsByLabnameAndStartBetween(labname, start, end);

    }

    public List<ReservedSlots> getAllLabReservedSlots(String labname) {
        return reservedSlotsRepository.getReservedSlotsByLabname(labname);
    }

    public List<ReservedSlots> getReservationsAfter (String labname, LocalDateTime time) {
        return reservedSlotsRepository.getReservedSlotsByLabnameAndStartAfter(labname,time);
    }

    public List<ReservedSlots> getReservationsBefore (String labname, LocalDateTime time) {
        return reservedSlotsRepository.getReservedSlotsByLabnameAndEndAfter(labname,time);
    }



//    public boolean findReservedSlotsByLabnameAndStartDateAndEndDate(String labname, LocalDateTime start, LocalDateTime end) {
//
//        //List<ReservedSlots> result = reservedSlotsRepository.getReservedSlotsByLabnameAndStartEqualsAndEndEquals(labname, start, end);
//
//        List<ReservedSlots> result = reservedSlotsRepository.getReservedSlotsByLabname(labname);
//
//
//        //List<ReservedSlots> result = reservedSlotsRepository.getReservedSlotsByLabnameAndStart(labname, start);
//        if (result.size() == 0) {
//            log.info("Checking for reserved dates none");
//        }
//
//
//
//        if (result.size() > 0) {
//            //result.forEach(res -> log.info("Found reserved date:{} {}", res.getStart(), res.getEnd()));
//
//            result.stream().filter(s -> (s.getStart().equals(start) && s.getEnd().equals(end)))
//                    .findFirst().ifPresent(reservedSlots -> reservedSlotsRepository.deleteById(reservedSlots.getId()));
//
//
//            return true;
//        }
//
//        return false;
//    }

    public void deleteReservedSlots(ReservedSlots reservedSlot) {
      reservedSlotsRepository.delete(reservedSlot);
    }

    public void deleteReservedSlots (List<ReservedSlots> resSlotsList) {
        reservedSlotsRepository.deleteAll(resSlotsList);
    }

}
