package com.example.golfreservation.service;

import com.example.golfreservation.domain.dto.FieldTimeDto;
import com.example.golfreservation.domain.dto.GolfFieldDto;
import com.example.golfreservation.domain.dto.ReservationDto;
import com.example.golfreservation.domain.entity.FieldTime;
import com.example.golfreservation.domain.entity.GolfField;
import com.example.golfreservation.domain.form.*;
import com.example.golfreservation.repository.GolfFieldRepo;
import com.example.golfreservation.repository.MemberRepo;
import com.example.golfreservation.repository.ReservationRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    GolfFieldRepo golfFieldRepo;

    @Autowired
    MemberRepo memberRepo;

    @Autowired
    ReservationRepo reservationRepo;

    @Autowired
    ReservationService reservationService;

    @Autowired
    GolfFieldService golfFieldService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    public void createGolfField() throws Exception {
        MemberForm memberForm = new MemberForm();
        memberForm.setLoginId("qwer");
        memberForm.setLoginPw("1234");
        memberForm.setName("joy");

        memberService.join(memberForm);

        LoginForm loginForm = new LoginForm();
        loginForm.setLoginId(memberForm.getLoginId());
        loginForm.setLoginPw(memberForm.getLoginPw());

        Long login = memberService.login(loginForm);

        if (login != null) {
            GolfFieldForm golfFieldForm = new GolfFieldForm();
            golfFieldForm.setMemberId(login);
            golfFieldForm.setFiledName("그린 골프");
            golfFieldForm.setLocation("충청도 어쩌구 저쩌구");
            golfFieldForm.setCapacity(5);
            golfFieldForm.setPrice(200000);

            golfFieldService.createMyGolfField(golfFieldForm);
            List<GolfFieldDto> myGolfField = golfFieldService.getMyGolfField(login);
            GolfFieldDto golfFieldDto = myGolfField.get(0);

            FieldTimeForm fieldTimeForm1 = new FieldTimeForm();
            fieldTimeForm1.setGolfFieldId(golfFieldDto.getId());
            fieldTimeForm1.setTime(12);

            FieldTimeForm fieldTimeForm2 = new FieldTimeForm();
            fieldTimeForm2.setGolfFieldId(golfFieldDto.getId());
            fieldTimeForm2.setTime(14);

            FieldTimeForm fieldTimeForm3 = new FieldTimeForm();
            fieldTimeForm3.setGolfFieldId(golfFieldDto.getId());
            fieldTimeForm3.setTime(16);

            golfFieldService.createFieldTime(fieldTimeForm1);
            golfFieldService.createFieldTime(fieldTimeForm2);
            golfFieldService.createFieldTime(fieldTimeForm3);
        }
    }

    @Test
    @DisplayName("예약하기")
    void createReservation() throws Exception {
        // given
        MemberForm memberForm = new MemberForm();
        memberForm.setLoginId("asdf");
        memberForm.setLoginPw("1234");
        memberForm.setName("wow");

        memberService.join(memberForm);

        LoginForm loginForm = new LoginForm();
        loginForm.setLoginId(memberForm.getLoginId());
        loginForm.setLoginPw(memberForm.getLoginPw());

        Long login = memberService.login(loginForm);

        if (login != null) {
            // when
            List<GolfField> golfFields = golfFieldRepo.findAll();
            GolfField reservationGolfField = golfFields.get(0);
            List<FieldTimeDto> fieldTimes = golfFieldService.getFieldTimes(reservationGolfField.getId());
            FieldTimeDto reservationTime = fieldTimes.get(1);

            reservationService.createReservation(login, reservationGolfField.getId(), reservationTime.getGolfFieldTimeId());
            List<ReservationDto> myReservation = reservationService.getMyReservation(login);
            ReservationDto reservationDto = myReservation.get(0);

            FieldTime fieldTime = golfFieldService.findFieldTime(reservationTime.getGolfFieldTimeId());

            // then
            Assertions.assertThat(reservationDto.getReservationName()).isEqualTo(memberForm.getName());
            Assertions.assertThat(reservationDto.getFieldName()).isEqualTo(reservationGolfField.getFieldName());
            Assertions.assertThat(reservationDto.getLocation()).isEqualTo(reservationGolfField.getLocation());
            Assertions.assertThat(reservationDto.getCapacity()).isEqualTo(reservationGolfField.getCapacity());
            Assertions.assertThat(reservationDto.getPrice()).isEqualTo(reservationGolfField.getPrice());
            Assertions.assertThat(reservationDto.getDateTime()).isEqualTo(reservationTime.getLocalDateTime().toString());
            Assertions.assertThat(fieldTime.getCheckState()).isTrue();
        }
    }

    @Test
    @DisplayName("예약 취소")
    void removeReservation() throws Exception {
        // given
        MemberForm memberForm = new MemberForm();
        memberForm.setLoginId("asdf");
        memberForm.setLoginPw("1234");
        memberForm.setName("joy");

        memberService.join(memberForm);

        LoginForm loginForm = new LoginForm();
        loginForm.setLoginId(memberForm.getLoginId());
        loginForm.setLoginPw(memberForm.getLoginPw());

        Long login = memberService.login(loginForm);

        if (login != null) {
            // when
            List<GolfField> golfFields = golfFieldRepo.findAll();
            GolfField reservationGolfField = golfFields.get(0);

            List<FieldTimeDto> fieldTimes = golfFieldService.getFieldTimes(reservationGolfField.getId());
            FieldTimeDto reservationFieldTime = fieldTimes.get(2);

            reservationService.createReservation(login, reservationGolfField.getId(), reservationFieldTime.getGolfFieldTimeId());

            List<ReservationDto> myReservation = reservationService.getMyReservation(login);
            ReservationDto reservationDto = myReservation.get(0);
            reservationService.removeReservation(reservationDto.getId());

            FieldTime fieldTime = golfFieldService.findFieldTime(reservationFieldTime.getGolfFieldTimeId());
            // then
            Assertions.assertThat(fieldTime.getCheckState()).isFalse();
        }
    }
}