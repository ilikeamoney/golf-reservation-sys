package com.example.golfreservation.controller;

import com.example.golfreservation.domain.entity.Authority;
import com.example.golfreservation.domain.entity.Member;
import com.example.golfreservation.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping("/")
    public String golfMain(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();

        Long memLog = (Long) session.getAttribute("memLog");

        if (memLog != null) {
            Authority authority = memberService.find(memLog).getAuthority();
            model.addAttribute("authority", authority.name());
            model.addAttribute("memLog", memLog);
        }

        return "view/main";
    }

}
