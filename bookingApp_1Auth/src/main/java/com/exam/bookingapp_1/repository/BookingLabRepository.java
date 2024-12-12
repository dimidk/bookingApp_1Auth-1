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

    boolean existsById(int id);

    List<BookingLab> getBookingLabsByLabname(String labname);

    Optional<BookingLab> getBookingLabById(int id);

    boolean existsBookingLabByLabnameAndStartBetween(String labname, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select max(b.id) from BookingLab b")
    int findTopIdBookingLab();

    @Override
    void deleteAllById(Iterable<? extends Integer> integers);

    List<BookingLab> getAllByLabnameAndStartBetween(String labname, LocalDateTime startTime, LocalDateTime endTime);
    List<BookingLab> getAllByLabnameAndEndBetween(String labname, LocalDateTime startTime, LocalDateTime endTime);
}
