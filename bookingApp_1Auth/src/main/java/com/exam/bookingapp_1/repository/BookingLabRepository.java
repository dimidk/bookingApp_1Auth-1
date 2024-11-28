package com.exam.bookingapp_1.repository;


import com.exam.bookingapp_1.model.BookingLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

@Repository
public interface BookingLabRepository extends JpaRepository <BookingLab, Integer>{


    List<BookingLab> findAll();
//    BookingLab getLastBookingLab();

    boolean existsById(int id);
    List<BookingLab> getBookingLabsByLabname(String labname);

    //@Query("update b.end from BookingLab b where b.id = ?")
    //BookingLab updateBookingLabById(int id, BookingLab bookingLab);

    Optional<BookingLab> getBookingLabById(int id);

    boolean existsBookingLabByLabnameAndStartBetween(String labname, LocalDateTime startTime, LocalDateTime endTime);

    boolean existsBookingLabByLabnameAndStartBetweenAndEndBefore(String labname, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime endTime2);
    boolean existsBookingLabByLabnameAndEndBetween(String labname, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select max(b.id) from BookingLab b")
    int findTopIdBookingLab();

}
