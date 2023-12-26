package com.example.golfreservation.error;

public class DuplicateMemberId extends Exception {

    private static final String MESSAGE = "중복되는 아이디 입니다.";

    public DuplicateMemberId() {
        super(MESSAGE);
    }
}
