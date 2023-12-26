package com.example.golfreservation.service;

import com.example.golfreservation.domain.edit.EditPassword;
import com.example.golfreservation.domain.entity.Authority;
import com.example.golfreservation.domain.entity.Member;
import com.example.golfreservation.domain.form.LoginForm;
import com.example.golfreservation.domain.form.MemberForm;
import com.example.golfreservation.error.DuplicateMemberId;
import com.example.golfreservation.error.NotExitMember;
import com.example.golfreservation.repository.MemberRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepo memberRepo;

    @BeforeEach
    public void removeAll() {
        memberRepo.deleteAll();
    }

    @Test
    @DisplayName("회원 가입")
    void memberJoin() throws Exception {
        // given
        MemberForm memberForm = new MemberForm();
        memberForm.setLoginId("qwer");
        memberForm.setLoginPw("1234");
        memberForm.setName("wow");

        // when
        memberService.join(memberForm);

        // then
        Assertions.assertThat(memberRepo.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인")
    void memberLogin() throws Exception {
        // given
        MemberForm memberForm = new MemberForm();
        memberForm.setLoginId("qwer");
        memberForm.setLoginPw("1234");
        memberForm.setName("wow");
        memberService.join(memberForm);

        LoginForm loginForm = new LoginForm();
        loginForm.setLoginId(memberForm.getLoginId());
        loginForm.setLoginPw(memberForm.getLoginPw());

        // when
        Long login = memberService.login(loginForm);

        // then
        Assertions.assertThat(login).isEqualTo(1L);
    }

    @Test
    @DisplayName("중복확인 검증")
    void duplicateId() throws Exception {
        // given
        MemberForm memberForm1 = new MemberForm();
        memberForm1.setLoginId("qwer");
        memberForm1.setLoginPw("1234");
        memberForm1.setName("wow");

        MemberForm memberForm2 = new MemberForm();
        memberForm2.setLoginId("qwer");
        memberForm2.setLoginPw("5555");
        memberForm2.setName("rrr");

        // when
        memberService.join(memberForm1);

        // then
        Assertions.assertThatThrownBy(() -> memberService.join(memberForm2))
                .isInstanceOf(DuplicateMemberId.class);
    }

    
    @Test
    @DisplayName("비밀번호 수정, 회원 찾기")
    void editPassword() throws Exception {
        // given
        MemberForm memberForm = new MemberForm();
        memberForm.setLoginId("qwer");
        memberForm.setLoginPw("1234");
        memberForm.setName("wow");

        memberService.join(memberForm);

        LoginForm loginForm = new LoginForm();
        loginForm.setLoginId(memberForm.getLoginId());
        loginForm.setLoginPw(memberForm.getLoginPw());
        Long login = memberService.login(loginForm);

        // when
        EditPassword editPassword = new EditPassword();
        editPassword.setId(login);
        editPassword.setLoginPw("5555");
        memberService.editPassword(editPassword);

        Member member = memberService.find(login);

        // then
        Assertions.assertThat(member.getLoginPw()).isEqualTo(editPassword.getLoginPw());
    }


    @Test
    @DisplayName("회원 권한 변경")
    void changeAuthority() throws Exception {
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
            Member findMember = memberService.find(login);
            Assertions.assertThat(findMember.getAuthority()).isEqualTo(Authority.USER);

            // then
            memberService.changeMemberAuthority(login);
            findMember = memberService.find(login);
            Assertions.assertThat(findMember.getAuthority()).isEqualTo(Authority.BUSINESS);
        }
    }
}