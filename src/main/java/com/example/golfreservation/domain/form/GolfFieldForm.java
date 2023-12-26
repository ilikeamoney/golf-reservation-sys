package com.example.golfreservation.domain.form;

import lombok.Data;

@Data
public class GolfFieldForm {

    private Long memberId;
    private String filedName;
    private String location;
    private Integer capacity;
    private Integer price;
}
