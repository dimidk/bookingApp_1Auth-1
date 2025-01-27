package com.exam.bookingapp_1.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import com.exam.bookingapp_1.model.BookingLab;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Slf4j
@Table(name="reservedslots")
public class ReservedSlots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="start")
    private LocalDateTime start;

    @Column(name="end")
    private LocalDateTime end;

    @Column(name="labname")
    private String labname;


    public boolean overlaps(@NotNull ReservedSlots reservedSlots) {

        return this.start.isBefore(reservedSlots.end) &&
                this.end.isAfter(reservedSlots.start);

    }



//
//
//    public boolean findOverlapDateTime(BookingLab bookingLab,int times,List<LocalDateTime> datesStart) {
//
//        int id = bookingLab.getId();
//        LocalDateTime startTime = bookingLab.getStart();
//        LocalDateTime endTime = bookingLab.getEnd();
//        log.info("findReservedDates: addingRepeating endTime date {}", endTime);
//
////        List<LocalDateTime> datesStart = reservedDatesStart(bookingLab.getLabname(), startTime, startTime.plusWeeks(times));
//        log.info("find dates from db where start between start and times {}", datesStart.stream().toList());
//
//        for (LocalDateTime date: datesStart) {
//            if (endTime.isAfter(date)) {}
//        }
//
//
//
////        datesStart.forEach(date -> {if (date.isBefore(endTime.plusWeeks(1))) {datesStart.remove(date);}});
//
//        List<LocalDateTime> createEndTime = IntStream.range(0, times + 1)
//                .mapToObj(weeks -> endTime.plusWeeks(weeks))
//                .collect(Collectors.toList());
//
//        createEndTime.forEach(date -> log.info("endTime for repeated event {}", String.valueOf(date)));
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
//        List<Long> leftDates = createEndTimeToMinutes.stream().flatMap(num -> datesStartToMinutes.stream().filter(d -> d.compareTo(num) > 0))
//                .collect(Collectors.toList());
//
//        List<LocalDateTime> leftDatesCorrect = leftDates.stream().map(d -> Instant.ofEpochSecond(d)
//                .atZone(ZoneId.of("UTC"))
//                .toLocalDateTime()
//        ).collect(Collectors.toList());
//
//        leftDatesCorrect.forEach(date -> log.info("converted date {}", String.valueOf(date)));
//
//
//        if (leftDatesCorrect.size() <= createEndTime.size()) {
//            return false;
//        }
//
//        return true;
//    }


}
