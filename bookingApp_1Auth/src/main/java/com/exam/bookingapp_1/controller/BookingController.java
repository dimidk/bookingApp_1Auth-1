package com.exam.bookingapp_1.controller;

import com.exam.bookingapp_1.model.BookingLab;
import com.exam.bookingapp_1.repository.BookingLabRepository;
import com.exam.bookingapp_1.service.BookingLabService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

//@Controller("/auth")
@Controller
@AllArgsConstructor
@Slf4j
public class BookingController {

    @Autowired
    private BookingLabService bookingLabService;
    @Autowired
    private BookingLabRepository bookingLabRepository;

    //display bookings for all labs
//    @RequestMapping(value = "/displayBookingsA", method = RequestMethod.GET)
//    public ResponseEntity<List<BookingLab>> getAllBookingsA() throws IOException {
//
//        List<BookingLab> allBookings = new ArrayList<>();
//        String labname = "ΔΕΠΥ Α";
//
//        allBookings = bookingLabService.getByLabname(labname);
//
//        return ResponseEntity.ok(allBookings);
//
//    }
//
//    @RequestMapping(value = "/displayBookingsB", method = RequestMethod.GET)
//    public ResponseEntity<List<BookingLab>> getAllBookingsB() throws IOException {
//
//        List<BookingLab> allBookings = new ArrayList<>();
//        String labname = "ΔΕΠΥ Β";
//
//        allBookings = bookingLabService.getByLabname(labname);
//
//        return ResponseEntity.ok(allBookings);
//
//    }
//
//
//    @RequestMapping(value = "/displayBookingsC", method = RequestMethod.GET)
//    public ResponseEntity<List<BookingLab>> getAllBookingsC() throws IOException {
//
//        List<BookingLab> allBookings = new ArrayList<>();
//        String labname = "ΔΕΠΥ Γ";
//
//        allBookings = bookingLabService.getByLabname(labname);
//
//        return ResponseEntity.ok(allBookings);
//
//    }


    @GetMapping("/allBookings")
    public ResponseEntity<String> getAllBookingsCount() {

        long num;

//        num = bookingLabService.countAll();
        num = bookingLabService.getBookingLabMaxId();
        return ResponseEntity.ok(String.valueOf(num));

    }

    private boolean bookingLabOnReservedDate(BookingLab bookingLab) {

//        if (bookingLabService.existsBookingLab(bookingLab.getId())
//                || bookingLabService.dateBookingLab(bookingLab)) {
//            return true;
//        }
        if (bookingLabService.dateBookingLab(bookingLab)) {
            return true;
        }

        return false;

    }


    //display all bookings for specific lab
    @GetMapping("/displayBookingInfo/{labName}")
    public List<BookingLab> getBookingLab(@PathVariable String labName) {

   //     List<BookingLab> bookingLab = bookingLabRepository.findByBTitle(labName);

//        List<BookingLab> bookingLab = bookingLabService.findBookingLabByName(labName);



        return bookingLabService.findBookingLabByName(labName);
    }

    //update booking lab
    @PostMapping("/updateBooking")
    public ResponseEntity<Optional<BookingLab>> updateBookingLab(@RequestBody  BookingLab bookingLab) {

        Optional<BookingLab> recordUpdated = null;
        String username = null;
    //first check the existence of the event
       recordUpdated = bookingLabService.getBookingLabById(bookingLab.getId());
       username = getAuthenticationUsername();
        //recordUpdated = bookingLabService.updateBookingLab(bookingLab);
        if (recordUpdated.isPresent() && recordUpdated.get().getUsername().equals(username)) {

            log.info("event with attributes {} {} {} updated", recordUpdated.get().getId(),recordUpdated.get().getStart(),bookingLab.getEnd());

            recordUpdated = bookingLabService.updateBookingLab(bookingLab);
            return ResponseEntity.ok(recordUpdated);
        }
        else {

            log.info("Either event doesn't exist or user has no permissions");
            return ResponseEntity.ofNullable(recordUpdated);

        }

    }


    @PostMapping("/newbooking")
    public ResponseEntity<Optional<BookingLab>> saveBookingLabRequest(@RequestBody BookingLab bookingLab) {

        BookingLab newEvent = null;

        log.info("create new booking lab with {}", bookingLab.getId());
        log.info("new event information {} {} {} {} {} {} ",bookingLab.getId(),bookingLab.getTitle(),bookingLab.getUsername(),bookingLab.getStart(), bookingLab.getEnd(),bookingLab.getLabname());

        log.info("end time presentation {}" ,bookingLab.getEnd());
        if (bookingLabOnReservedDate(bookingLab)) {

            log.info("booking lab {} already exists or trying to book on reserved date {} ", bookingLab.getId(), bookingLab.getStart());
        }
        else {

            newEvent = BookingLab.builder()
                    .id(bookingLab.getId())
                    .title(bookingLab.getTitle())
                    .start(bookingLab.getStart())
                    .end(bookingLab.getEnd())
                    .username(bookingLab.getUsername())
                    .labname(bookingLab.getLabname())
                    .build();

            log.info("logging the new Event {} {} {} {} {} {} :",newEvent.getId(),newEvent.getTitle(),newEvent.getStart(),newEvent.getEnd(),newEvent.getUsername(),newEvent.getLabname());
            bookingLabService.add(newEvent);
            //bookingLabService.add(bookingLab);
        }

        return ResponseEntity.ok(Optional.ofNullable(newEvent));
    }

    private void addingRepeatingEvents(BookingLab bookingLab,int times) {

        BookingLab newEvent = null;
        int id = bookingLab.getId();
        LocalDateTime startTime = bookingLab.getStart();
        LocalDateTime endTime = bookingLab.getEnd();

        for (int i = 0; i <= times; i++) {

            newEvent = BookingLab.builder()
                    .id(id)
                    .title(bookingLab.getTitle())
                    .start(startTime)
                    .end(endTime)
                    .username(bookingLab.getUsername())
                    .labname(bookingLab.getLabname())
                    .build();

            bookingLabService.add(newEvent);

            id++;
            startTime = startTime.plusWeeks(1);
            endTime = endTime.plusWeeks(1);

        }

    }

    @PostMapping("/recurbooking/{dateStart}/{dateEnd}")
    public ResponseEntity<HttpStatus> saveRecurBooking(@RequestBody  BookingLab bookingLab, @PathVariable String dateStart, @PathVariable String dateEnd, Locale locale) {

        Optional<String> response = null;
        BookingLab newEvent = null;


        String time = bookingLab.getStart().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        log.info("first booking lab event with start date and with id {} {}" ,bookingLab.getStart(),bookingLab.getId());

        LocalDateTime getTimeStart = LocalDateTime.parse(dateStart + "T" + time);
        LocalDateTime getTimeEnd = LocalDateTime.parse(dateEnd + "T" + time);

        log.info("start date and time for recursion {}", getTimeStart);
        log.info("end date and time for recursion {}", getTimeEnd);

        log.info("new event information {} {} {} {} {} {} ",bookingLab.getId(),bookingLab.getTitle(),bookingLab.getUsername(),bookingLab.getStart(),
                bookingLab.getEnd(),bookingLab.getLabname());
        log.info("add a recursing event same time from date {} to {}", dateStart, dateEnd);

        if (bookingLabOnReservedDate(bookingLab)) {

            log.info("booking lab {} already exists or trying to book on reserved date {} ", bookingLab.getId(), bookingLab.getStart());
        }
        else {

            long numberOfWeeks = ChronoUnit.WEEKS.between(LocalDate.parse(dateStart), LocalDate.parse(dateEnd));
            log.info("number of weeks to reserve the event: {}", numberOfWeeks);

            addingRepeatingEvents(bookingLab,Integer.parseInt(Long.toString(numberOfWeeks)));

        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private String  getAuthenticationUsername() {
        String username = null;

        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        username = authUser.getName();

        return username;

    }

    @PostMapping("/deleteBooking")
    public ResponseEntity<BookingLab> deleteBooking(@RequestBody BookingLab bookingLab) {

        Optional<BookingLab> recordDeleted = null;
        BookingLab notFound = null;

        log.info("event to delete: {} {} {} ",bookingLab.getId(),bookingLab.getTitle(),bookingLab.getStart());

        recordDeleted = bookingLabService.getBookingLabById(bookingLab.getId());
        log.info("delete booking lab {}", recordDeleted.get().getId());

        String username = getAuthenticationUsername();
        log.info("user logged in {}", username);
        //log.info("user in booking lab {}", recordDeleted.getUsername());

        log.info("user in booking lab {}", recordDeleted.get().getUsername());

        if (!username.equals(recordDeleted.get().getUsername())) {
            log.info("You are not authorized to delete this event");

            //return ResponseEntity.ofNullable(recordDeleted);

            return ResponseEntity.ofNullable(notFound);
        }
        else {

           boolean a = bookingLabService.deleteBookingLab(bookingLab.getId());
        }
        return ResponseEntity.of(recordDeleted);

    }


    @GetMapping("/bookingLab/{id}")
    public ResponseEntity<BookingLab> getBookingLabById(@PathVariable String id) {

        Optional<BookingLab> bookingLab = null;

        bookingLab = bookingLabRepository.getBookingLabById(Integer.parseInt(id));

        return ResponseEntity.of(bookingLab);
        //return ResponseEntity.ok(bookingLab);

    }

}
