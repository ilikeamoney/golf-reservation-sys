package com.example.golfreservation.domain.form;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FieldTimeForm {
    private final Integer year = LocalDateTime.now().getYear();
    private final Integer month = LocalDateTime.now().getMonthValue();
    private final Integer day = LocalDateTime.now().getDayOfMonth();
    private Integer time;
    private Long golfFieldId;
}
