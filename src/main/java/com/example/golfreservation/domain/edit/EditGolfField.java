package com.example.golfreservation.domain.edit;

import lombok.Data;

@Data
public class EditGolfField {
    private String filedName;
    private String location;
    private Integer capacity;
    private Integer price;
}
