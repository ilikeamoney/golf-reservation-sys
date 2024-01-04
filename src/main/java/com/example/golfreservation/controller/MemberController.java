package com.example.golfreservation.controller;

import com.example.golfreservation.domain.form.LoginForm;
import com.example.golfreservation.domain.form.MemberForm;
import com.example.golfreservation.repository.MemberRepo;
import com.example.golfreservation.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberRepo memberRepo;

    @GetMapping("/join")
    public String joinGet() {
        return "view/member/01_member_join";
    }

    @PostMapping("/join")
    public String joinPost(@ModelAttribute MemberForm memberForm) {
        memberService.join(memberForm);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginGet() {
        return "view/member/02_member_login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute LoginForm loginForm,
                            HttpServletRequest req) {
        HttpSession session = req.getSession();

        Long login = memberService.login(loginForm);

        if (login != null) {
            session.setAttribute("memLog", login);
        }

        return "redirect:/";
    }

    @GetMapping("/remove-member")
    public String removeMemberGet() {
        return "view/member/03_member_remove";
    }

    @PostMapping("/remove-member")
    public String removeMemberPost(HttpServletRequest req,
                                   @ModelAttribute LoginForm loginForm) {
        HttpSession session = req.getSession();
        Long login = memberService.login(loginForm);

        if (login != null) {
            memberService.removeMember(login);
            session.invalidate();
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logoutGet(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/change-authority")
    public String changeAuthority(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Long memLog = (Long)session.getAttribute("memLog");

        memberService.changeMemberAuthority(memLog);

        return "redirect:/";
    }

}
