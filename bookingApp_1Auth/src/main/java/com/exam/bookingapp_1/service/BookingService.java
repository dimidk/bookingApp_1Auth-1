package com.exam.bookingapp_1.service;

import com.exam.bookingapp_1.model.BookingLab;
import com.exam.bookingapp_1.model.ReservedSlots;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
 * Booking Service responsible for updating, adding, deleting bookings
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingLabService bookingLabService;

    private final ReservedSlotsService reservedSlotsService;


    public int numAllBookings() { return  bookingLabService.getBookingLabMaxId();}

    public Optional<BookingLab> updateBooking(BookingLab bookingLab) {

        Optional<BookingLab> updated = bookingLabService.updateBookingLab(bookingLab);

        return updated;

    }

    public BookingLab addNewBooking(BookingLab bookingLab) {

        ReservedSlots newResSlots = ReservedSlots.builder()
                .labname(bookingLab.getLabname())
                .end(bookingLab.getEnd())
                .start(bookingLab.getStart())
                .build();

        BookingLab newEvent = BookingLab.builder()
                .id(bookingLab.getId())
                .title(bookingLab.getTitle())
                .start(bookingLab.getStart())
                .end(bookingLab.getEnd())
                .username(bookingLab.getUsername())
                .labname(bookingLab.getLabname())
                .build();

        log.info("logging the new Event {} {} {} {} {} {} :",newEvent.getId(),newEvent.getTitle(),newEvent.getStart(),newEvent.getEnd(),newEvent.getUsername(),newEvent.getLabname());
        bookingLabService.add(newEvent);
        reservedSlotsService.add(newResSlots);

        ReservedSlots check = new ReservedSlots();
        check.setStart(LocalDateTime.of(2024,12,5,13,30));
        check.setEnd(LocalDateTime.of(2024,12,5,14,30));
        log.info("New Reservation Slot overlaps: {}",newResSlots.overlaps(check));

        return newEvent;
    }

    public boolean addingRepeatingEvents(BookingLab bookingLab,int times) {

        BookingLab newEvent = null;
        ReservedSlots  resSlots = new ReservedSlots();

        int id = bookingLab.getId();
        LocalDateTime startTime = bookingLab.getStart();
        LocalDateTime endTime = bookingLab.getEnd();
//        log.info("addingRepeating endTime date {}",endTime);

        //problem with the query
        List<ReservedSlots> reservedSlotsList = reservedSlotsService.getAllLabReservedSlots(bookingLab.getLabname());
//        reservedSlotsList.forEach(reservedSlots -> log.info("query result: start date :{}",reservedSlots.getStart()));

        // List<LocalDateTime> datesStart = reservedDatesStart(bookingLab.getLabname(),startTime,startTime.plusWeeks(times));

        List<ReservedSlots> newReserved = IntStream.range(0,times+1)
                .mapToObj(i -> {

                    return ReservedSlots.builder()
                            .build();
                })
                .toList();
        int i=0;
        LocalDateTime start = startTime;
        LocalDateTime end = endTime;
        for(ReservedSlots reservedSlot : newReserved) {

            reservedSlot.setStart(start.plusWeeks(i));
            reservedSlot.setEnd(end.plusWeeks(i));
            i++;

        }

//        newReserved.forEach(resslot -> log.info("new reservation date {} {}",resslot.getStart(),resslot.getEnd()));

        List<BookingLab> addBookings = new ArrayList<>();
        List<ReservedSlots> addReservedSlots = new ArrayList<>();
        List<Boolean> canReserved = new ArrayList<>();

        for ( i = 0; i <= times; i++) {

            newEvent = BookingLab.builder()
                    .id(id+i)
                    .title(bookingLab.getTitle())
                    .start(startTime.plusWeeks(i))
                    .end(endTime.plusWeeks(i))
                    .username(bookingLab.getUsername())
                    .labname(bookingLab.getLabname())
                    .build();

            ReservedSlots newResSlot = ReservedSlots.builder()
                    .start(startTime.plusWeeks(i))
                    .end(endTime.plusWeeks(i))
                    .labname(bookingLab.getLabname())
                    .build();
            addBookings.add(newEvent);
            addReservedSlots.add(newResSlot);

            canReserved.add(canReserve(reservedSlotsList,newResSlot));
            log.info("New Reservation Slot overlaps: {} {}",canReserve(reservedSlotsList,newResSlot),
                    newResSlot.getStart());
        }
        int countFalse = Math.toIntExact(canReserved.stream().filter(value -> !value).count());
        if (countFalse > 0) {
            log.info("No possibility to reserve lab on these dates");
            return false;
        }
        bookingLabService.addAll(addBookings);
        reservedSlotsService.addAll(addReservedSlots);

        return true;
    }

    private boolean canReserve(List<ReservedSlots> resSlots,ReservedSlots newResSlot) {

        boolean a = false;
        a = resSlots.stream().noneMatch(existing -> {

//                    log.info("{} {} {}:",existing.overlaps(newResSlot),existing.getStart(),existing.getEnd());
//                    log.info("{} {} {}:",newResSlot.overlaps(existing),newResSlot.getStart(),newResSlot.getEnd());
                    return existing.overlaps(newResSlot) || newResSlot.overlaps(existing);
                }
        );

        //return resSlots.stream().noneMatch(existing -> existing.overlaps(newResSlot) || newResSlot.overlaps(existing));
        return a;
    }


    public Optional<BookingLab> deleteBooking(BookingLab bookingLab) {

        Optional<BookingLab> recordDeleted = bookingLabService.getBookingLabById(bookingLab.getId());

        boolean a = bookingLabService.deleteBookingLab(bookingLab.getId());
        ReservedSlots resSlot = ReservedSlots.builder()
                .labname(bookingLab.getLabname())
                .start(bookingLab.getStart())
                .end(bookingLab.getEnd())
                .build();
        reservedSlotsService.deleteReservedSlots(resSlot);

        log.info("Booking Deleted with the corresponding TimeSlot");

        return recordDeleted;
    }

    private List<ReservedSlots> findResSlotsToDelete(BookingLab bookingLab,int times) {


        List<Integer> ids = IntStream.range(bookingLab.getId(),bookingLab.getId()+times+1)
                .boxed().toList();

        List<ReservedSlots> resSlotsList = reservedSlotsService.getReservedSlotsBetween(bookingLab.getLabname(),
                bookingLab.getStart(),
                bookingLab.getStart().plusWeeks(Long.parseLong(Integer.toString(times+1))));
        resSlotsList.forEach(res -> log.info("record for deletion:{} {} {}",res.getLabname(),res.getStart(),res.getEnd()));
        //resSlotsList.stream().forEach(res -> log.info("record for deletion:{} {} {}",res.getLabname(),res.getStart(),res.getEnd()));

        List<LocalDateTime> bookings = ids.stream().map(i -> bookingLabService.getBookingLabById(i)
                .map(BookingLab::getStart).orElseThrow()).toList();
        //    .map(d -> d.getStart()).orElseThrow()).toList();

        List<ReservedSlots> finalForDeletion = resSlotsList.stream().filter(date -> bookings.stream().anyMatch(b -> b.isEqual(date.getStart())))
                .collect(Collectors.toList());

        //if finalForDeletion is Empty proceeds with the deletion of bookings
        finalForDeletion.forEach(res -> log.info("delete the record:{} {} {}",res.getLabname(),res.getStart(),res.getEnd()));

        //finalForDeletion.stream().forEach(res -> log.info("delete the record:{} {} {}",res.getLabname(),res.getStart(),res.getEnd()));

        return finalForDeletion;
    }

    public void deleteRepeatingBookings(BookingLab bookingLab, int weeksNum) {

        //get all events from id to id+numberOfWeeks and delete them instead of the loop
            List<Integer> ids = IntStream.range(bookingLab.getId(),bookingLab.getId()+weeksNum+1)
                    .boxed().toList();

            reservedSlotsService.deleteReservedSlots(findResSlotsToDelete(bookingLab,weeksNum));

            //bookingLabRepository.deleteAllById(ids);
            bookingLabService.deleteRecurBookingLab(ids);
    }
}
