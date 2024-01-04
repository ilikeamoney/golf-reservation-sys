package com.example.golfreservation.service;

import com.example.golfreservation.domain.dto.ReservationDto;
import com.example.golfreservation.domain.entity.FieldTime;
import com.example.golfreservation.domain.entity.GolfField;
import com.example.golfreservation.domain.entity.Member;
import com.example.golfreservation.domain.entity.Reservation;
import com.example.golfreservation.repository.ReservationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final MemberService memberService;

    private final GolfFieldService golfFieldService;

    private final ReservationRepo reservationRepo;

    public Reservation find(Long reservationId) {
        return reservationRepo.findById(reservationId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ReservationDto> getMyReservation(Long memId) {
        List<ReservationDto> reservationDtos = new ArrayList<>();

        for (Reservation reservation : reservationRepo.getMyReservation(memId)) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(reservation.getId());
            reservationDto.setReservationName(reservation.getMember().getName());
            reservationDto.setFieldName(reservation.getGolfField().getFieldName());
            reservationDto.setLocation(reservation.getGolfField().getLocation());
            reservationDto.setCapacity(reservation.getGolfField().getCapacity());
            reservationDto.setPrice(reservation.getGolfField().getPrice());
            reservationDto.setDateTime(reservation.getFieldTime().getLocalDateTime().toString());
            reservationDtos.add(reservationDto);
        }
        return reservationDtos;
    }

    public void createReservation(Long memId, Long golfFieldId, Long fieldTimeId) {
        GolfField golfField = golfFieldService.findField(golfFieldId);
        FieldTime fieldTime = golfFieldService.findFieldTime(fieldTimeId);
        Member findMember = memberService.find(memId);

        if (findMember != null && null != golfField && fieldTime != null) {
            fieldTime.changeDisable();
            reservationRepo.save(
                    Reservation.builder()
                            .member(findMember)
                            .golfField(golfField)
                            .fieldTime(fieldTime)
                            .build()
            );
        }
    }

    public void removeReservation(Long reservationId) {
        Reservation reservation = find(reservationId);

        if (reservation != null) {
            reservation.getFieldTime().changeAble();
            reservationRepo.deleteById(reservationId);
        }
    }

}
