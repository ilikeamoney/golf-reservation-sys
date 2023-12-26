package com.example.golfreservation.error;

public class NotExitMember extends Exception {
    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public NotExitMember() {
        super(MESSAGE);
    }
}
