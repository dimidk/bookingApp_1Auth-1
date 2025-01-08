package com.exam.bookingapp_1.repository;

import com.exam.bookingapp_1.model.ReservedSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservedSlotsRepository extends JpaRepository<ReservedSlots, Integer> {

    //List<LocalDateTime> getReservedSlotsByStartAndStartBetween(LocalDateTime start, LocalDateTime end);

    //List<ReservedSlots> getReservedSlotsByStartAndStartBetween(LocalDateTime start, LocalDateTime end);

    List<ReservedSlots> getReservedSlotsByStartBetween(LocalDateTime start, LocalDateTime end);

    List<ReservedSlots> getReservedSlotsByLabnameAndStartBetween(String labname, LocalDateTime start, LocalDateTime end);

    @Override
    void deleteAll(Iterable<? extends ReservedSlots> entities);

    public void deleteAllByStartBetween(LocalDateTime start, LocalDateTime end);

    public List<ReservedSlots> getReservedSlotsByLabname(String labname);

    public List<ReservedSlots> getReservedSlotsByLabnameAndStartAfter(String labname, LocalDateTime start);
    public List<ReservedSlots> getReservedSlotsByLabnameAndEndAfter(String labname, LocalDateTime end);
}
