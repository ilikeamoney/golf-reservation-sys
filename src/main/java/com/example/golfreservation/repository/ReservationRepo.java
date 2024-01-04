package com.example.golfreservation.repository;

import com.example.golfreservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {


    @Query("SELECT r FROM Reservation r WHERE r.member.id = :memId")
    List<Reservation> getMyReservation(@Param("memId") Long memId);

    @Query("SELECT r FROM Reservation r WHERE r.golfField.id = :golfFieldId")
    List<Reservation> getGolfFieldReservation(@Param("golfFieldId") Long golfFieldId);
}
