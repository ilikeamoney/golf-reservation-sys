package com.example.golfreservation.controller;

import com.example.golfreservation.service.GolfFieldService;
import com.example.golfreservation.service.MemberService;
import com.example.golfreservation.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final MemberService memberService;
    private final GolfFieldService golfFieldService;
    private final ReservationService reservationService;

    @GetMapping("/reservation")
    public String reservation(@RequestParam("fieldId") Long fieldId,
                              @RequestParam("fieldTimeId") Long fieldTimeId,
                              HttpServletRequest req) {
        Long memLog = (Long) req.getSession().getAttribute("memLog");
        reservationService.createReservation(memLog, fieldId, fieldTimeId);
        return "redirect:/field-detail-user/" + fieldId;
    }


    @GetMapping("/my-reservation")
    public String myReservation(HttpServletRequest req,
                                Model model) {
        Long memLog = (Long) req.getSession().getAttribute("memLog");

        model.addAttribute("myReservation", reservationService.getMyReservation(memLog));
        return "view/reservation/01_my_reservation";
    }

    @GetMapping("/delete-reservation")
    public String deleteReservation(@RequestParam("reId") Long reId) {

        reservationService.removeReservation(reId);

        return "redirect:/my-reservation";
    }
}
