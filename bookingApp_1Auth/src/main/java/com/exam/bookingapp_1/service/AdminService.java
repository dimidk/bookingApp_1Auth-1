package com.exam.bookingapp_1.service;

import com.exam.bookingapp_1.model.BookingLab;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/***
 * Admin Service responsible for loading old bookings and export database
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final BookingService bookingService;

    private List<String> updateNewSemester(List<BookingLab> bookingLabs) {

        bookingLabs.forEach(bookingLab -> {
            log.info("getting info for each booking {} {} ",bookingLab.getStart(),bookingLab.getEnd());
        });

        //get day and time for specific user and lab

        List<Map.Entry<LocalDateTime,LocalDateTime>> bookedDate =
                bookingLabs.stream()
                        .map(bookingLab -> Map.entry(bookingLab.getStart(),bookingLab.getEnd()))
                        .toList();

        List<String> dayAndTime = bookedDate.stream()
                .map(date -> date.getKey().getDayOfWeek() + "-" + date.getKey().toLocalTime()
                        + "-" + date.getValue().toLocalTime())
                .toList();

        //count the occurence of each booking
        Map<String, Long> occurs =  dayAndTime.stream()
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));

        //get those bookings that are repeated more than once
        List<String> selected = occurs.entrySet().stream()
                .filter(str -> str.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

        selected.forEach(e -> log.info(e.toString()));

        return selected;

    }

    public void createSemesterBooking(List<BookingLab> bookings, LocalDate startSemester) {

        String labname = bookings.get(0).getLabname();
        log.info("Booking is about to create for lab {}", labname);

        //holds the dates and times and the new semester date start
        List<String> dates = updateNewSemester(bookings);
        Map<List<String>,LocalDate> dateMap =  new HashMap<>();
        dateMap.put(dates,startSemester);

        List<String> bookingDetails = new ArrayList<>();
        bookingDetails.add(labname);
        bookingDetails.add(bookings.get(0).getTitle());
        bookingDetails.add(bookings.get(0).getUsername());

        addSemesterBooking(dateMap,bookingDetails);

    }

    private void addSemesterBooking(Map<List<String>, LocalDate> dateMap, List<String> bookingDetails) {

        List<String> daysAndTime = dateMap.entrySet().stream()
                .map(Map.Entry::getKey).toList().get(0);

        //this is the value of the key in the map
        LocalDate startDateSemester = dateMap.get(daysAndTime);
        log.info("Semester starts {}", startDateSemester);


        for (int i = 0; i < daysAndTime.size(); i++) {

            String day = daysAndTime.get(i).split("-")[0];
            String timeStart = daysAndTime.get(i).split("-")[1];
            String timeEnd = daysAndTime.get(i).split("-")[2];

//           log.info("weekday {} {}",startDateSemester.getDayOfWeek(),startDateSemester.getDayOfWeek().getValue());
//           if (DayOfWeek.valueOf(day).getValue() - startDateSemester.getDayOfWeek().getValue() > 0) {
            int sub = DayOfWeek.valueOf(day).getValue() - startDateSemester.getDayOfWeek().getValue();
//           log.info("{}",sub);

            log.info("Semester's starting week {}",startDateSemester.plusDays(sub));


            LocalDate firstDay = startDateSemester.plusDays(sub);
            LocalDateTime bookingStartDate = LocalDateTime.of(firstDay,
                    LocalTime.of(Integer.parseInt(timeStart.split(":")[0]),
                            Integer.parseInt(timeStart.split(":")[1])));

            LocalDateTime bookingEndDate = LocalDateTime.of(firstDay,
                    LocalTime.of(Integer.parseInt(timeEnd.split(":")[0]),
                            Integer.parseInt(timeEnd.split(":")[1])));

            log.info("booking start date {} and end date {}",bookingStartDate,bookingEndDate);

            BookingLab bookingLab = BookingLab.builder()
                    .id(bookingService.numAllBookings()+1)
                    .labname(bookingDetails.get(0))
                    .title(bookingDetails.get(1))
                    .username(bookingDetails.get(2))
                    .start(bookingStartDate)
                    .end(bookingEndDate)
                    .build();

            bookingService.addingRepeatingEvents(bookingLab,13);

        }
    }
}
