package com.example.golfreservation.controller;

import com.example.golfreservation.domain.dto.FieldTimeDto;
import com.example.golfreservation.domain.dto.GolfFieldDto;
import com.example.golfreservation.domain.entity.GolfField;
import com.example.golfreservation.domain.form.FieldTimeForm;
import com.example.golfreservation.domain.form.GolfFieldForm;
import com.example.golfreservation.service.GolfFieldService;
import com.example.golfreservation.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GolfFieldController {

    private final GolfFieldService golfFieldService;

    private final MemberService memberService;

    @GetMapping("/create-my-golfField")
    public String createGolfFieldGet() {
        return "view/golfField/01_golfField_create";
    }

    @PostMapping("/create-my-golfField")
    public String createFieldPost(@ModelAttribute GolfFieldForm golfFieldForm,
                                  @RequestParam("locationDetail") String locationDetail,
                                  HttpServletRequest req) {

        HttpSession session = req.getSession();
        Long memId = (Long) session.getAttribute("memLog");
        golfFieldForm.setMemberId(memId);
        golfFieldForm.setLocation(golfFieldService.editLocation(golfFieldForm.getLocation(), locationDetail));
        golfFieldService.createMyGolfField(golfFieldForm);

        return "redirect:/";
    }

    @GetMapping("/my-golf-fields")
    public String myGolfFields(HttpServletRequest req,
                               Model model) {
        HttpSession session = req.getSession();
        Long memLog = (Long) session.getAttribute("memLog");
        List<GolfFieldDto> myGolfField = golfFieldService.getMyGolfField(memLog);

        if (myGolfField != null) {
            model.addAttribute("memberName", memberService.find(memLog).getName());
            model.addAttribute("golfFields", myGolfField);
        }

        return "view/golfField/02_my_golf_fields";
    }

    @GetMapping("/field-detail/{fieldId}")
    public String fieldDetailGet(@PathVariable("fieldId") Long fieldId,
                              Model model) {
        GolfField golfField = golfFieldService.findField(fieldId);
        List<FieldTimeDto> fieldTimes = golfFieldService.getFieldTimes(fieldId);
        FieldTimeForm fieldTimeForm = new FieldTimeForm();

        if (golfField != null) {
            model.addAttribute("golfField", golfField);
            model.addAttribute("fieldTimes", fieldTimes);
            model.addAttribute("fieldTimeForm", fieldTimeForm);
        }

        return "view/golfField/03_field_detail";
    }

    @PostMapping("/field-detail/{fieldId}")
    public String fieldDetailPost(@ModelAttribute FieldTimeForm fieldTimeForm,
                                  @PathVariable("fieldId") Long fieldId) {

        golfFieldService.createFieldTime(fieldTimeForm);
        return "redirect:/field-detail/" + fieldId;
    }

    @GetMapping("/golf-fields")
    public String golfFields(Model model) {
        model.addAttribute("golfFields", golfFieldService.getAll());
        return "view/golfField/04_golf_fields";
    }

    @GetMapping("/field-detail-user/{fieldId}")
    public String golfFieldDetail(@PathVariable("fieldId") Long fieldId,
                                  HttpServletRequest req,
                                  Model model) {
        HttpSession session = req.getSession();
        Long memLog = (Long) session.getAttribute("memLog");

        GolfField golfField = golfFieldService.findField(fieldId);
        List<FieldTimeDto> fieldTimes = golfFieldService.getFieldTimes(golfField.getId());

        model.addAttribute("field", golfField);
        model.addAttribute("fieldTimes", fieldTimes);
        model.addAttribute("fieldAdminId", golfField.getMember().getId());
        model.addAttribute("memLog", memLog);

        return "view/golfField/05_field_detail_user";
    }

    @GetMapping("/edit-field-time")
    public String editFieldTime(@RequestParam("golfFieldId") Long golfFieldId,
                                @RequestParam("fieldTimeId") Long fieldTimeId,
                                @RequestParam("time") Integer time) {
        golfFieldService.editFieldTime(fieldTimeId, time);
        return "redirect:/field-detail/" + golfFieldId;
    }


    @GetMapping("/delete-field-time")
    public String deleteFieldTime(@RequestParam("fieldId") Long fieldId,
                                  @RequestParam("fieldTimeId") Long fieldTimeId) {

        golfFieldService.removeFieldTime(fieldTimeId);
        return "redirect:/field-detail/" + fieldId;
    }

    @GetMapping("/delete-golf-field")
    public String deleteField(@RequestParam("fieldId") Long fieldId) {

        golfFieldService.removeField(fieldId);

        return "redirect:/my-golf-fields";
    }
}
