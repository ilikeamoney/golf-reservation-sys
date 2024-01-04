package com.example.golfreservation.service;

import com.example.golfreservation.domain.dto.GolfFieldDto;
import com.example.golfreservation.domain.edit.EditGolfField;
import com.example.golfreservation.domain.entity.FieldTime;
import com.example.golfreservation.domain.entity.GolfField;
import com.example.golfreservation.domain.entity.Member;
import com.example.golfreservation.domain.dto.FieldTimeDto;
import com.example.golfreservation.domain.form.FieldTimeForm;
import com.example.golfreservation.domain.form.GolfFieldForm;
import com.example.golfreservation.repository.FieldTimeRepo;
import com.example.golfreservation.repository.GolfFieldRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GolfFieldService {

    private final MemberService memberService;

    private final GolfFieldRepo golfFieldRepo;

    private final FieldTimeRepo fieldTimeRepo;

    @Transactional(readOnly = true)
    public GolfField findField(Long golfFieldId) {
        return golfFieldRepo.findById(golfFieldId).orElse(null);
    }

    @Transactional(readOnly = true)
    public FieldTime findFieldTime(Long fieldTimeId) {
        return fieldTimeRepo.findById(fieldTimeId).orElse(null);
    }

    public void createMyGolfField(GolfFieldForm golfFieldForm) {
        Member findMember = memberService.find(golfFieldForm.getMemberId());
        if (findMember != null) {
            golfFieldRepo.save(GolfField.builder()
                    .fieldName(golfFieldForm.getFiledName())
                    .location(golfFieldForm.getLocation())
                    .capacity(golfFieldForm.getCapacity())
                    .price(golfFieldForm.getPrice())
                    .member(findMember)
                    .build());
        }
    }

    public void editMyGolfField(Long golfFieldId, EditGolfField editGolfField) {
        GolfField golfField = findField(golfFieldId);

        if (golfField != null) {
            golfField.editField(editGolfField);
        }
    }

    public void removeField(Long golfFieldId) {
        GolfField golfField = findField(golfFieldId);

        if (golfField != null) {
            for (FieldTime fieldTime : golfField.getFieldTimes()) {
                fieldTimeRepo.deleteById(fieldTime.getId());
            }

            golfFieldRepo.deleteById(golfFieldId);
        }
    }

    @Transactional(readOnly = true)
    public List<GolfFieldDto> getMyGolfField(Long memId) {
        List<GolfFieldDto> fieldDtos = new ArrayList<>();
        Member findMember = memberService.find(memId);

        for (GolfField golfField : findMember.getMyGolfFieldList()) {
            GolfFieldDto golfFieldDto = new GolfFieldDto();
            golfFieldDto.setId(golfField.getId());
            golfFieldDto.setFieldName(golfField.getFieldName());
            golfFieldDto.setLocation(golfField.getLocation());
            golfFieldDto.setCapacity(golfField.getCapacity());
            golfFieldDto.setPrice(golfField.getPrice());

            fieldDtos.add(golfFieldDto);
        }

        return fieldDtos;
    }

    public void createFieldTime(FieldTimeForm fieldTimeForm) {
        GolfField golfField = findField(fieldTimeForm.getGolfFieldId());

        if (golfField != null) {
            Integer year = fieldTimeForm.getYear();
            Integer month = fieldTimeForm.getMonth();
            Integer day = fieldTimeForm.getDay();
            Integer time = fieldTimeForm.getTime();

            if (time >= 1 && 24 >= time) {
                fieldTimeRepo.save(FieldTime.builder()
                        .golfField(golfField)
                        .localDateTime(LocalDateTime.of(year, month, day, time, 0))
                        .build());
            }

        }
    }

    public void editFieldTime(Long fieldTimeId, Integer time) {
        FieldTime fieldTime = findFieldTime(fieldTimeId);

        if (fieldTime != null) {
            if (time >= 1 && 24 >= time) {
                FieldTimeForm fieldTimeForm = new FieldTimeForm();
                fieldTimeForm.setTime(time);

                LocalDateTime editTime = LocalDateTime.of(fieldTimeForm.getYear(), fieldTimeForm.getMonth(), fieldTimeForm.getDay(), fieldTimeForm.getTime(), 0);
                fieldTime.editTime(editTime);
            }
        }
    }

    public void removeFieldTime(Long fieldTimeId) {
        fieldTimeRepo.deleteById(fieldTimeId);
    }

    public List<FieldTimeDto> getFieldTimes(Long golfFieldId) {
        List<FieldTimeDto> fieldTimeDtos = new ArrayList<>();

        for (FieldTime fieldTime : findField(golfFieldId).getFieldTimes()) {
            FieldTimeDto fieldTimeDto = new FieldTimeDto();

            fieldTimeDto.setGolfFieldTimeId(fieldTime.getId());
            fieldTimeDto.setLocalDateTime(fieldTime.getLocalDateTime());
            fieldTimeDto.setState(fieldTime.getCheckState());

            fieldTimeDtos.add(fieldTimeDto);
        }

        return fieldTimeDtos;
    }

    public String editLocation(String location, String locationDetail) {
        return String.valueOf(location + "-" + locationDetail);
    }

    public List<GolfFieldDto> getAll() {
        List<GolfFieldDto> golfFieldDtos = new ArrayList<>();

        for (GolfField golfField : golfFieldRepo.findAll()) {
            GolfFieldDto golfFieldDto = new GolfFieldDto();

            golfFieldDto.setId(golfField.getId());
            golfFieldDto.setFieldName(golfField.getFieldName());
            golfFieldDto.setLocation(golfField.getLocation());
            golfFieldDto.setCapacity(golfField.getCapacity());
            golfFieldDto.setPrice(golfField.getPrice());

            golfFieldDtos.add(golfFieldDto);
        }

        return golfFieldDtos;
    }
}
