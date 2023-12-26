package com.example.golfreservation.domain.form;

import lombok.Data;

@Data
public class MemberForm {
    private String loginId;
    private String loginPw;
    private String name;
}
