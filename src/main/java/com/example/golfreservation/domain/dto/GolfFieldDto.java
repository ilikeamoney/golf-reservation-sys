package com.example.golfreservation.domain.dto;

import lombok.Data;

@Data
public class GolfFieldDto {
    private Long id;
    private String fieldName;
    private String location;
    private Integer capacity;
    private Integer price;
}
