package com.exam.bookingapp_1.controller;

import com.exam.bookingapp_1.model.BookingLab;
import com.exam.bookingapp_1.model.Busers;
import com.exam.bookingapp_1.model.Role;
import com.exam.bookingapp_1.repository.BookingLabRepository;
import com.exam.bookingapp_1.service.*;
import com.exam.bookingapp_1.model.ReservedSlots;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.mail.MessagingException;
import javax.swing.text.html.Option;


@Controller
@AllArgsConstructor
@Slf4j
public class BookingController {

    //because of problems instead of Autowired use private final and @AllArgsConstructor annotation
    //to avoid any bug issues

    //@Autowired
    private final BookingLabService bookingLabService;

    private final BookingService bookingService;

//    //@Autowired
//    private final BookingLabRepository bookingLabRepository;
//
//    //@Autowired
//    private final ReservedSlotsService reservedSlotsService;

    //@Autowired
    private final SendEmailService sendEmailService;

    //@Autowired
    private final BusersService busersService;


    /***
     *Create a list with reserved dates in order to avoid overlap when batch reservation is
     * made by a user.
     * But there is an error that the first date that is free is reserved but the others reserverd
     * are not re-reserved
     *
     *
     */

    @GetMapping("/allBookings")
    public ResponseEntity<String> getAllBookingsCount() {

        long num;

//        num = bookingLabService.getBookingLabMaxId();
        num = bookingService.numAllBookings();
        return ResponseEntity.ok(String.valueOf(num));

    }


    //display all bookings for specific lab
    @GetMapping("/displayBookingInfo/{labName}")
    public List<BookingLab> getBookingLab(@PathVariable String labName) {

        return bookingLabService.findBookingLabByName(labName);
    }

    //update booking lab
    @PostMapping("/updateBooking")
    public ResponseEntity<Optional<BookingLab>> updateBookingLab(@RequestBody  BookingLab bookingLab) {

    //first check the existence of the even

        Optional<BookingLab> recordUpdated = bookingLabService.getBookingLabById(bookingLab.getId());
        String username = getAuthenticationUsername();
        //recordUpdated = bookingLabService.updateBookingLab(bookingLab);
        if (recordUpdated.isPresent() && recordUpdated.get().getUsername().equals(username)) {

            log.info("event with attributes {} {} {} updated", recordUpdated.get().getId(),recordUpdated.get().getStart(),bookingLab.getEnd());

            //old design
            //recordUpdated = bookingLabService.updateBookingLab(bookingLab);

            recordUpdated = bookingService.updateBooking(bookingLab);
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

        newEvent = bookingService.addNewBooking(bookingLab);
        return ResponseEntity.ok(Optional.ofNullable(newEvent));
    }



    public boolean canReserve(List<ReservedSlots> resSlots,ReservedSlots newResSlot) {

        boolean a = false;
        a = resSlots.stream().noneMatch(existing -> {

            log.info("{} {} {}:",existing.overlaps(newResSlot),existing.getStart(),existing.getEnd());
            log.info("{} {} {}:",newResSlot.overlaps(existing),newResSlot.getStart(),newResSlot.getEnd());
            return existing.overlaps(newResSlot) || newResSlot.overlaps(existing);
        }
        );

        //return resSlots.stream().noneMatch(existing -> existing.overlaps(newResSlot) || newResSlot.overlaps(existing));
        return a;
    }

    @PostMapping("/recurbooking/{dateStart}/{dateEnd}")
    public ResponseEntity<HttpStatus> saveRecurBooking(@RequestBody  BookingLab bookingLab, @PathVariable String dateStart, @PathVariable String dateEnd) {

        Optional<String> response = null;
        BookingLab newEvent = null;


        String time = bookingLab.getStart().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        log.info("first booking lab event with start date and with id {} {}" ,bookingLab.getStart(),bookingLab.getId());

        //LocalDateTime getTimeStart = LocalDateTime.parse(dateStart + "T" + time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime getTimeStart = LocalDateTime.parse(dateStart + "T" + time);
        LocalDateTime getTimeEnd = LocalDateTime.parse(dateEnd + "T" + time);

        log.info("start date and time for recursion {}", getTimeStart);
        log.info("end date and time for recursion {}", getTimeEnd);

        log.info("new event information {} {} {} {} {} {} ",bookingLab.getId(),bookingLab.getTitle(),bookingLab.getUsername(),bookingLab.getStart(),
                bookingLab.getEnd(),bookingLab.getLabname());
        log.info("add a recursing event same time from date {} to {}", dateStart, dateEnd);
        long numberOfWeeks = ChronoUnit.WEEKS.between(LocalDate.parse(dateStart), LocalDate.parse(dateEnd));
        log.info("number of weeks to reserve the event: {}", numberOfWeeks);

        if (!bookingService.addingRepeatingEvents(bookingLab,Integer.parseInt(Long.toString(numberOfWeeks)))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private String  getAuthenticationUsername() {
        String username = null;

        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();

        username = authUser.getName();

        return username;

    }

    private Authentication getAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/deleteBooking")
    public ResponseEntity<BookingLab> deleteBooking(@RequestBody BookingLab bookingLab) throws MessagingException {

        BookingLab notFound = null ;

        log.info("event to delete: {} {} {} ",bookingLab.getId(),bookingLab.getTitle(),bookingLab.getStart());

        Optional<BookingLab> recordDeleted = bookingLabService.getBookingLabById(bookingLab.getId());
        log.info("delete booking lab {}", recordDeleted.get().getId());

        String username = getAuthenticationUsername();
        log.info("user logged in {}", username);
        //log.info("user in booking lab {}", recordDeleted.getUsername());

        log.info("user in booking lab {}", recordDeleted.get().getUsername());



        if (!username.equals(recordDeleted.get().getUsername()) && getAuthentication().getAuthorities().equals(Role.valueOf("USER"))) {
            log.info("You are not authorized to delete this event");

            //return ResponseEntity.ofNullable(recordDeleted);

            return ResponseEntity.ofNullable(notFound);
        }


        recordDeleted = bookingService.deleteBooking(bookingLab);
        HashMap<String,String> params = prepareEmailParams();
//           sendEmail.emailParams(params);
        sendEmailService.sendNewMail(params.get("To"), params.get("Subject"), params.get("Body"));
        String to_sec = "mkyrieri@central.ntua.gr";
        sendEmailService.sendNewMail(to_sec, params.get("Subject"), params.get("Body"));

        return ResponseEntity.of(recordDeleted);

    }

    private HashMap<String,String> prepareEmailParams() {

        HashMap<String,String> params = new HashMap<>();


        Busers user = busersService.getUserEmail(getAuthenticationUsername());
//        String to  = user.getEmail();
        String to = "dimideka.dimi@gmail.com";

        params.put("To",to);

        String subject = "Days and Times of reservation Lab changed!";
        params.put("Subject",subject);

        String body = "Dear user " + getAuthenticationUsername() + ",\n\n";
        body = body + "Τα διατμηματικά εργαστήρια είναι ελεύθερα για κάποιες μέρες και ώρες μετά από ακυρώσεις.  \n";
        body = body + "Παρακαλώ ελέγξτε την περίπτωση αν επιθυμείτε να κάνετε κράτηση αυτές τις μέρες.  \n\n";
        body = body + "Με εκτίμηση \n\n" + "Central of National Technical University of Athens";

        params.put("Body",body);
        return params;
    }

    @PostMapping("/recurDeleteBooking/{startDate}/{endDate}")
//    public ResponseEntity<BookingLab> recurDeleteBooking(@RequestBody BookingLab bookingLab,@PathVariable String startDate,@PathVariable String endDate) {
    public ResponseEntity<HttpStatus> recurDeleteBooking(@RequestBody BookingLab bookingLab,@PathVariable String startDate,@PathVariable String endDate) {

        BookingLab notFound = null;

        Optional<BookingLab> recordDeleted = bookingLabService.getBookingLabById(bookingLab.getId());
        if (recordDeleted.isEmpty()) {
            throw new RuntimeException("there is no such a booking event");
        }
        String username = getAuthenticationUsername();
        long numberOfWeeks = ChronoUnit.WEEKS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
        int weeksNum = Integer.parseInt(Long.toString(numberOfWeeks));

        log.info("number of weeks to delete the event {} for user  {}", numberOfWeeks, username);

        if (!username.equals(recordDeleted.get().getUsername()) && getAuthentication().getAuthorities().equals(Role.valueOf("USER"))) {
            log.info("You are not authorized to delete this event");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        bookingService.deleteRepeatingBookings(recordDeleted.get(),weeksNum);
        HashMap<String,String> params = prepareEmailParams();
        sendEmailService.sendNewMail(params.get("To"), params.get("Subject"), params.get("Body"));


        return  ResponseEntity.status(HttpStatus.OK).build();

    }


    @GetMapping("/bookingLab/{id}")
    public ResponseEntity<BookingLab> getBookingLabById(@PathVariable String id) {

        Optional<BookingLab> bookingLab ;

        //bookingLab = bookingLabRepository.getBookingLabById(Integer.parseInt(id));

        bookingLab = bookingLabService.getBookingLabById(Integer.parseInt(id));
        return ResponseEntity.of(bookingLab);
        //return ResponseEntity.ok(bookingLab);

    }

}
