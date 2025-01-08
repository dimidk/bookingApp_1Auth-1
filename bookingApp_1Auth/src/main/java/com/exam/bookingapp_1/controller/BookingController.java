package com.exam.bookingapp_1.controller;

import com.exam.bookingapp_1.model.BookingLab;
import com.exam.bookingapp_1.model.Busers;
import com.exam.bookingapp_1.repository.BookingLabRepository;
import com.exam.bookingapp_1.service.BookingLabService;
import com.exam.bookingapp_1.model.ReservedSlots;
import com.exam.bookingapp_1.service.BusersService;
import com.exam.bookingapp_1.service.ReservedSlotsService;
import com.exam.bookingapp_1.service.SendEmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.UnsupportedEncodingException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.mail.MessagingException;


@Controller
@AllArgsConstructor
@Slf4j
public class BookingController {

    @Autowired
    private BookingLabService bookingLabService;
    @Autowired
    private BookingLabRepository bookingLabRepository;
    @Autowired
    private ReservedSlotsService reservedSlotsService;

    @Autowired
    private SendEmailService sendEmailService;

    private final BusersService busersService;


    /***
     *Create a list with reserved dates in order to avoid overlap when batch reservation is
     * made by a user.
     *
     * But there is an error that the first date that is free is reserved but the others reserverd
     * are not re-reserved
     *
     * @return
     */

    @GetMapping("/allBookings")
    public ResponseEntity<String> getAllBookingsCount() {

        long num;

//        num = bookingLabService.countAll();
        num = bookingLabService.getBookingLabMaxId();
        return ResponseEntity.ok(String.valueOf(num));

    }

    private boolean bookingLabOnReservedDate(String labname) {


    return true;


    }


    //display all bookings for specific lab
    @GetMapping("/displayBookingInfo/{labName}")
    public List<BookingLab> getBookingLab(@PathVariable String labName) {

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
//        if (bookingLabOnReservedDate(bookingLab.getLabname())) {
//
//            log.info("booking lab {} already exists or trying to book on reserved date {} ", bookingLab.getId(), bookingLab.getStart());
//        }
//        else {
        ReservedSlots newResSlots = ReservedSlots.builder()
                .labname(bookingLab.getLabname())
                .end(bookingLab.getEnd())
                .start(bookingLab.getStart())
                .build();

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
            reservedSlotsService.add(newResSlots);

            ReservedSlots check = new ReservedSlots();
            check.setStart(LocalDateTime.of(2024,12,5,13,30));
            check.setEnd(LocalDateTime.of(2024,12,5,14,30));
            log.info("checking reserved slot {}:",newResSlots.overlaps(check));

            //bookingLabService.add(bookingLab);
       // }

        return ResponseEntity.ok(Optional.ofNullable(newEvent));
    }


//    private List<LocalDateTime> reservedDatesEnd(String labname, LocalDateTime start, LocalDateTime end) {
//
//        List<BookingLab> bookings = new ArrayList<>();
//
//        bookings = bookingLabService.getBookingLabsByLabnameAndDateEnd(labname,start,end);
//        log.info("booking dates ending from db between start and end {}",bookings.stream().map(BookingLab::getEnd).toList());
//
//        List<LocalDateTime> dates = bookings.stream().map(BookingLab::getEnd).collect(Collectors.toList());
//        return dates;
//    }

//    private boolean findOverlapDateTime(BookingLab bookingLab,int times) {
//
//        int id = bookingLab.getId();
//        LocalDateTime startTime = bookingLab.getStart();
//        LocalDateTime endTime = bookingLab.getEnd();
//        log.info("findReservedDates: addingRepeating endTime date {}",endTime);
//
//        List<LocalDateTime> datesStart = reservedDatesStart(bookingLab.getLabname(),startTime,startTime.plusWeeks(times));
//        log.info("find dates from db where start between start and times {}",datesStart.stream().toList());
//
//
////        datesStart.forEach(date -> {if (date.isBefore(endTime.plusWeeks(1))) {datesStart.remove(date);}});
//
//        List<LocalDateTime> createEndTime = IntStream.range(0,times+1)
//                .mapToObj(weeks -> endTime.plusWeeks(weeks))
//                .collect(Collectors.toList());
//
//        createEndTime.forEach(date -> log.info("endTime for repeated event {}",String.valueOf(date)));
//
//        List<Long> createEndTimeToMinutes = createEndTime.stream()
//                .mapToLong(date -> date.toEpochSecond(ZoneOffset.UTC))
//                .boxed()
//                .collect(Collectors.toList());
//
//        List<Long> datesStartToMinutes = datesStart.stream()
//                .mapToLong(date -> date.toEpochSecond(ZoneOffset.UTC))
//                .boxed()
//                .collect(Collectors.toList());
//
//        List<Long> leftDates = createEndTimeToMinutes.stream().flatMap(num -> datesStartToMinutes.stream().filter(d -> d.compareTo(num) > 0 ))
//                .collect(Collectors.toList());
//
//        List<LocalDateTime> leftDatesCorrect = leftDates.stream().map(d -> Instant.ofEpochSecond(d)
//                .atZone(ZoneId.of("UTC"))
//                .toLocalDateTime()
//                ).collect(Collectors.toList());
//
//        leftDatesCorrect.forEach(date -> log.info("converted date {}",String.valueOf(date)));
//
////       Set<LocalDateTime> leftDates = createEndTime.stream().flatMap(date ->
////               datesStart.stream().filter(d ->  date.isEqual(d) || date.isAfter(d))).collect(Collectors.toSet());
//
//
//
//        //ελέγχει ότι το date  είναι μεταγενέστερη του d
////        Set<LocalDateTime> leftDates = datesStart.stream()
////                .flatMap((date) -> createEndTime.stream().filter(d ->
////                date.isEqual(d) || date.isAfter(d))).collect(Collectors.toSet());
//
//
//
////        List<LocalDate> leftDates = datesStart.stream().map(LocalDateTime::toLocalDate).flatMap(date ->
////                createEndTime.stream().map(LocalDateTime::toLocalDate).filter(d -> d.equals(date) || d.isAfter(date)))
////                .collect(Collectors.toList());
//        //leftDates.forEach(date -> log.info("date that is left {}",String.valueOf(date)));
//
//
//        if (leftDatesCorrect.size() <= createEndTime.size()) {
//            return false;
//        }
//
//        return true;
//    }


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


    private boolean addingRepeatingEvents(BookingLab bookingLab,int times) {

        BookingLab newEvent = null;
        ReservedSlots  resSlots = new ReservedSlots();

        int id = bookingLab.getId();
        LocalDateTime startTime = bookingLab.getStart();
        LocalDateTime endTime = bookingLab.getEnd();
        log.info("addingRepeating endTime date {}",endTime);

        //problem with the query
        List<ReservedSlots> reservedSlotsList = reservedSlotsService.getAllLabReservedSlots(bookingLab.getLabname());
        reservedSlotsList.stream().forEach(reservedSlots -> log.info("query result: start date :{}",reservedSlots.getStart()));

       // List<LocalDateTime> datesStart = reservedDatesStart(bookingLab.getLabname(),startTime,startTime.plusWeeks(times));

        List<ReservedSlots> newReserved = IntStream.range(0,times+1)
                .mapToObj(i -> {

                    return ReservedSlots.builder()
                            .build();
                        })
                .collect(Collectors.toList());
        int i=0;
        LocalDateTime start = startTime;
        LocalDateTime end = endTime;
        for(ReservedSlots reservedSlot : newReserved) {

            reservedSlot.setStart(start.plusWeeks(i));
            reservedSlot.setEnd(end.plusWeeks(i));
            i++;

        }

        newReserved.stream().forEach(resslot -> log.info("new reservation date {} {}",resslot.getStart(),resslot.getEnd()));
        //canReserveSlots(reservedSlotsList,newReserved);
//        if (!canReserveSlots(reservedSlotsList,newReserved)) {
//            return false;
//        }
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
            log.info("can reserve check new slot: {} {}",canReserve(reservedSlotsList,newResSlot),
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

    @PostMapping("/recurbooking/{dateStart}/{dateEnd}")
    public ResponseEntity<HttpStatus> saveRecurBooking(@RequestBody  BookingLab bookingLab, @PathVariable String dateStart, @PathVariable String dateEnd) {

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
        long numberOfWeeks = ChronoUnit.WEEKS.between(LocalDate.parse(dateStart), LocalDate.parse(dateEnd));
        log.info("number of weeks to reserve the event: {}", numberOfWeeks);

        if (!addingRepeatingEvents(bookingLab,Integer.parseInt(Long.toString(numberOfWeeks)))) {
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

    @PostMapping("/deleteBooking")
    public ResponseEntity<BookingLab> deleteBooking(@RequestBody BookingLab bookingLab) throws MessagingException {

        Optional<BookingLab> recordDeleted = null;
        BookingLab notFound = null;
        ReservedSlots resSlot = null;

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
           resSlot = ReservedSlots.builder()
                   .labname(bookingLab.getLabname())
                   .start(bookingLab.getStart())
                   .end(bookingLab.getEnd())
                   .build();
           reservedSlotsService.deleteReservedSlots(resSlot);

           HashMap<String,String> params = prepareEmailParams();
//           sendEmail.emailParams(params);
            sendEmailService.sendNewMail(params.get("To"), params.get("Subject"), params.get("Body"));
        }
        return ResponseEntity.of(recordDeleted);

    }

    private List<ReservedSlots> findResSlotsToDelete(BookingLab bookingLab,int times) {


        List<Integer> ids = IntStream.range(bookingLab.getId(),bookingLab.getId()+times+1)
                .boxed().toList();

        List<ReservedSlots> resSlotsList = reservedSlotsService.getReservedSlotsBetween(bookingLab.getLabname(),bookingLab.getStart(),bookingLab.getStart().plusWeeks(times+1));
        resSlotsList.stream().forEach(res -> log.info("record for deletion:{} {} {}",res.getLabname(),res.getStart(),res.getEnd()));

        List<LocalDateTime> bookings = ids.stream().map(i -> bookingLabService.getBookingLabById(i)
                .map(d -> d.getStart()).orElseThrow()).toList();
        List<ReservedSlots> finalForDeletion = resSlotsList.stream().filter(date -> bookings.stream().anyMatch(b -> b.isEqual(date.getStart())))
                .collect(Collectors.toList());

        //if finalForDeletion is Empty proceeds with the deletion of bookings
        finalForDeletion.stream().forEach(res -> log.info("delete the record:{} {} {}",res.getLabname(),res.getStart(),res.getEnd()));

        return finalForDeletion;
    }

    private HashMap<String,String> prepareEmailParams() {

        HashMap<String,String> params = new HashMap<>();


        Busers user = busersService.getUserEmail(getAuthenticationUsername());
        String to  = user.getEmail();
        //String to = "dekadimi@mail.ntua.gr";
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

        Optional<BookingLab> recordDeleted = null;
        BookingLab notFound = null;
        List<ReservedSlots> resSlotsList = null;

        recordDeleted = bookingLabService.getBookingLabById(bookingLab.getId());
        if (recordDeleted.isEmpty()) {
            throw new RuntimeException("there is no such a booking event");
        }
        String username = getAuthenticationUsername();
        long numberOfWeeks = ChronoUnit.WEEKS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
        int weeksNum = Integer.parseInt(Long.toString(numberOfWeeks));

        log.info("number of weeks to delete the event {} for user  {}", numberOfWeeks, username);

        if (!username.equals(recordDeleted.get().getUsername())) {
            log.info("You are not authorized to delete this event");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else {
            //get all events from id to id+numberOfWeeks and delete them instead of the loop
            List<Integer> ids = IntStream.range(bookingLab.getId(),bookingLab.getId()+weeksNum+1)
                    .boxed().toList();

            reservedSlotsService.deleteReservedSlots(findResSlotsToDelete(recordDeleted.get(),weeksNum));
            //bookingLabRepository.deleteAllById(ids);
            bookingLabService.deleteRecurBookingLab(ids);
//            HashMap<String,String> params = prepareEmailParams();
//            sendEmail.emailParams(params);
        }

        return  ResponseEntity.status(HttpStatus.OK).build();

    }


    @GetMapping("/bookingLab/{id}")
    public ResponseEntity<BookingLab> getBookingLabById(@PathVariable String id) {

        Optional<BookingLab> bookingLab = null;

        bookingLab = bookingLabRepository.getBookingLabById(Integer.parseInt(id));

        return ResponseEntity.of(bookingLab);
        //return ResponseEntity.ok(bookingLab);

    }

}
