package com.exam.bookingapp_1.service;

import com.exam.bookingapp_1.model.BookingLab;
import com.exam.bookingapp_1.repository.BookingLabRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BookingLabService {

  //  @Autowired
    private final BookingLabRepository bookingLabRepository;


    public void add(BookingLab bookingLab) {
        bookingLabRepository.save(bookingLab);
    }

    public void addAll(List<BookingLab> bookingLabs) {
        bookingLabRepository.saveAll(bookingLabs);
    }


    public List<BookingLab> getAll() {
        return bookingLabRepository.findAll();
    }

    public List<BookingLab> getByLabname(String labname) {
        return bookingLabRepository.getBookingLabsByLabname(labname);
    }

    public int getBookingLabMaxId() {
        return bookingLabRepository.findTopIdBookingLab();
    }

    public List<BookingLab> findBookingLabByName(String title) {

        return bookingLabRepository.getBookingLabsByLabname(title);
    }

    public boolean deleteBookingLab(int id) {

        bookingLabRepository.deleteById(id);
        return true;
    }


    public boolean deleteRecurBookingLab(List<Integer> ids) {

        bookingLabRepository.deleteAllById(ids);

        return true;

    }

    public Optional<BookingLab> updateBookingLab(BookingLab bookingLab) {

        Optional<BookingLab> updated = null;

        updated = bookingLabRepository.getBookingLabById(bookingLab.getId());
        log.info("BookingLabService: get info for record to be update {} {} {}", updated.get().getLabname(),updated.get().getStart(), updated.get().getEnd());
        log.info("this record to be updated with {} {}",bookingLab.getLabname(),bookingLab.getEnd());


        log.info("Updating booking lab");
        log.info("update booking with new date {} ", bookingLab.getEnd());
        updated.get().setEnd(bookingLab.getEnd());

        bookingLabRepository.save(updated.get());


        return updated;
    }


    public boolean dateBookingLab(BookingLab bookingLab) {

        boolean a = false;

        if (bookingLabRepository.existsBookingLabByLabnameAndStartBetween(bookingLab.getLabname(), bookingLab.getStart(), bookingLab.getEnd())) {
            a = true;
        }

        return a;

    }

    public List<BookingLab> getBookingLabsByLabnameAndDateStart(String labname, LocalDateTime startDate, LocalDateTime endDate) {

        return bookingLabRepository.getAllByLabnameAndStartBetween(labname, startDate, endDate);
    }

    public List<BookingLab> getBookingLabsByLabnameAndDateEnd(String labname, LocalDateTime startDate, LocalDateTime endDate) {

        return bookingLabRepository.getAllByLabnameAndEndBetween(labname, startDate, endDate);
    }

    public List<BookingLab> getBookingLabsName(List<BookingLab> bookingLabs, String labname) {

        List<BookingLab> bookingLabsList = bookingLabs.stream().filter(bookingLab -> bookingLab.getLabname().compareTo(labname) == 0)
                .collect(Collectors.toList());

        return bookingLabsList;


    }

    public List<BookingLab> getBookingsByLabnameAndUsername(String labname, String username) {

        return bookingLabRepository.findByUsernameAndLabname(labname, username);
    }

    public Optional<BookingLab> getBookingLabById(int bookingLabId) {
       return bookingLabRepository.getBookingLabById(bookingLabId);
    }

}
