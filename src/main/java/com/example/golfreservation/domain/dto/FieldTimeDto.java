package com.example.golfreservation.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FieldTimeDto {

    private Long golfFieldTimeId;

    private Boolean state;

    private LocalDateTime localDateTime;

}
