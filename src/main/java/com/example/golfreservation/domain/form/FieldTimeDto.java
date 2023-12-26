package com.example.golfreservation.domain.form;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FieldTimeDto {

    private Long golfFieldTimeId;

    private LocalDateTime localDateTime;

}
