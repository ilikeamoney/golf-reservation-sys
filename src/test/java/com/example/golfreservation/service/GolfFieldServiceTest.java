package com.example.golfreservation.service;

import com.example.golfreservation.domain.dto.FieldTimeDto;
import com.example.golfreservation.domain.dto.GolfFieldDto;
import com.example.golfreservation.domain.edit.EditGolfField;
import com.example.golfreservation.domain.entity.FieldTime;
import com.example.golfreservation.domain.entity.GolfField;
import com.example.golfreservation.domain.form.*;
import com.example.golfreservation.repository.GolfFieldRepo;
import com.example.golfreservation.repository.MemberRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GolfFieldServiceTest {

    @Autowired
    private GolfFieldService golfFieldService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private GolfFieldRepo golfFieldRepo;

    @Autowired
    private MemberRepo memberRepo;


    @BeforeEach
    public void deleteAll() {
        golfFieldRepo.deleteAll();
        memberRepo.deleteAll();
    }

    @Test
    @DisplayName("나의 골프장 생성")
    void createGolfField() throws Exception {
        // given
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
            // when
            GolfFieldForm golfFieldForm = new GolfFieldForm();
            golfFieldForm.setMemberId(login);
            golfFieldForm.setFiledName("그린 골프");
            golfFieldForm.setLocation("충청도");
            golfFieldForm.setCapacity(5);
            golfFieldForm.setPrice(200000);

            golfFieldService.createMyGolfField(golfFieldForm);
            List<GolfFieldDto> myGolfField = golfFieldService.getMyGolfField(login);

            // then
            Assertions.assertThat(myGolfField.get(0).getFieldName()).isEqualTo(golfFieldForm.getFiledName());
            Assertions.assertThat(myGolfField.size()).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("나의 골프장 수정")
    void editGolfField() throws Exception {
        // given
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
            // when
            GolfFieldForm golfFieldForm = new GolfFieldForm();
            golfFieldForm.setMemberId(login);
            golfFieldForm.setFiledName("그린 골프");
            golfFieldForm.setLocation("충정도 어쩌구 저쩌구");
            golfFieldForm.setCapacity(5);
            golfFieldForm.setPrice(200000);

            golfFieldService.createMyGolfField(golfFieldForm);

            List<GolfFieldDto> myGolfField = golfFieldService.getMyGolfField(login);
            GolfFieldDto golfFieldDto = myGolfField.get(0);

            EditGolfField editGolfFIeld = new EditGolfField();
            editGolfFIeld.setLocation("경기도 어쩌구 저쩌구");
            editGolfFIeld.setPrice(250000);

            golfFieldService.editMyGolfField(golfFieldDto.getId(), editGolfFIeld);
            GolfField findGolfField = golfFieldService.findField(golfFieldDto.getId());

            // then
            Assertions.assertThat(findGolfField.getLocation()).isEqualTo(editGolfFIeld.getLocation());
            Assertions.assertThat(findGolfField.getPrice()).isEqualTo(editGolfFIeld.getPrice());
        }
    }

    @Test
    @DisplayName("나의 골프장 삭제")
    void removeGolfField() throws Exception {
        // given
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
            // when
            GolfFieldForm golfFieldForm = new GolfFieldForm();
            golfFieldForm.setMemberId(login);
            golfFieldForm.setFiledName("그린 골프");
            golfFieldForm.setLocation("충청도 어쩌구 저쩌구");
            golfFieldForm.setPrice(20000);
            golfFieldForm.setCapacity(5);

            golfFieldService.createMyGolfField(golfFieldForm);
            List<GolfFieldDto> myGolfField = golfFieldService.getMyGolfField(login);
            GolfFieldDto golfFieldDto = myGolfField.get(0);

            // then
            Assertions.assertThat(myGolfField.size()).isEqualTo(1);
            golfFieldService.removeField(golfFieldDto.getId());
            myGolfField = golfFieldService.getMyGolfField(login);
            Assertions.assertThat(myGolfField.size()).isEqualTo(0);

        }
    }

    @Test
    @DisplayName("골프장 시간 생성")
    void createFieldTime() throws Exception {
        // given
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
            // when
            GolfFieldForm golfFieldForm = new GolfFieldForm();
            golfFieldForm.setMemberId(login);
            golfFieldForm.setFiledName("그린 골프");
            golfFieldForm.setLocation("충청도");
            golfFieldForm.setCapacity(5);
            golfFieldForm.setPrice(200000);

            golfFieldService.createMyGolfField(golfFieldForm);
            List<GolfFieldDto> myGolfField = golfFieldService.getMyGolfField(login);
            GolfFieldDto golfFieldDto = myGolfField.get(0);

            FieldTimeForm fieldTimeForm1 = new FieldTimeForm();
            fieldTimeForm1.setGolfFieldId(golfFieldDto.getId());
            fieldTimeForm1.setTime(14);

            FieldTimeForm fieldTimeForm2 = new FieldTimeForm();
            fieldTimeForm2.setGolfFieldId(golfFieldDto.getId());
            fieldTimeForm2.setTime(16);

            FieldTimeForm fieldTimeForm3 = new FieldTimeForm();
            fieldTimeForm3.setGolfFieldId(golfFieldDto.getId());
            fieldTimeForm3.setTime(18);

            golfFieldService.createFieldTime(fieldTimeForm1);
            golfFieldService.createFieldTime(fieldTimeForm2);
            golfFieldService.createFieldTime(fieldTimeForm3);

            List<FieldTimeDto> fieldTimes = golfFieldService.getFieldTimes(golfFieldDto.getId());

            // then
            Assertions.assertThat(fieldTimes.size()).isEqualTo(3);
        }
    }


    @Test
    @DisplayName("골프장 시간 수정")
    void editGolfFieldTime() throws Exception {
        // given
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
            // when
            GolfFieldForm golfFieldForm = new GolfFieldForm();
            golfFieldForm.setMemberId(login);
            golfFieldForm.setFiledName("그린 골프");
            golfFieldForm.setLocation("충청도");
            golfFieldForm.setCapacity(5);
            golfFieldForm.setPrice(200000);

            golfFieldService.createMyGolfField(golfFieldForm);
            List<GolfFieldDto> myGolfField = golfFieldService.getMyGolfField(login);
            GolfFieldDto golfFieldDto = myGolfField.get(0);

            FieldTimeForm fieldTimeForm = new FieldTimeForm();
            fieldTimeForm.setGolfFieldId(golfFieldDto.getId());
            fieldTimeForm.setTime(14);
            golfFieldService.createFieldTime(fieldTimeForm);

            List<FieldTimeDto> fieldTimes = golfFieldService.getFieldTimes(golfFieldDto.getId());
            FieldTimeDto fieldTimeDto = fieldTimes.get(0);
            golfFieldService.editFieldTime(fieldTimeDto.getGolfFieldTimeId(), 20);

            FieldTime fieldTime = golfFieldService.findFieldTime(fieldTimeDto.getGolfFieldTimeId());
            int hour = fieldTime.getLocalDateTime().getHour();

            // then
            Assertions.assertThat(hour).isEqualTo(20);

        }
    }

    @Test
    @DisplayName("골프장 시간 삭제")
    void deleteGolfFieldTime() throws Exception {
        // given
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
            // when
            GolfFieldForm golfFieldForm = new GolfFieldForm();
            golfFieldForm.setMemberId(login);
            golfFieldForm.setFiledName("그린 골프");
            golfFieldForm.setLocation("충청도 어쩌구 저쩌구");
            golfFieldForm.setCapacity(5);
            golfFieldForm.setPrice(200000);

            golfFieldService.createMyGolfField(golfFieldForm);
            List<GolfFieldDto> myGolfField = golfFieldService.getMyGolfField(login);
            GolfFieldDto golfFieldDto = myGolfField.get(0);

            FieldTimeForm fieldTimeForm = new FieldTimeForm();
            fieldTimeForm.setGolfFieldId(golfFieldDto.getId());
            fieldTimeForm.setTime(20);

            golfFieldService.createFieldTime(fieldTimeForm);
            List<FieldTimeDto> fieldTimes = golfFieldService.getFieldTimes(golfFieldDto.getId());
            FieldTimeDto fieldTimeDto = fieldTimes.get(0);
            golfFieldService.removeFieldTime(fieldTimeDto.getGolfFieldTimeId());

            fieldTimes = golfFieldService.getFieldTimes(golfFieldDto.getId());

            // then
            Assertions.assertThat(fieldTimes.size()).isEqualTo(0);
        }
    }




}