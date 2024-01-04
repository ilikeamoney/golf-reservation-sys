package com.example.golfreservation.domain.dto;

import lombok.Data;

@Data
public class ReservationDto {
    private Long id;
    private String reservationName;
    private String fieldName;
    private String location;
    private String dateTime;
    private Integer capacity;
    private Integer price;
}
