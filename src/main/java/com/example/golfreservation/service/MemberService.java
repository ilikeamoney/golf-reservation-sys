package com.example.golfreservation.service;

import com.example.golfreservation.domain.edit.EditPassword;
import com.example.golfreservation.domain.entity.FieldTime;
import com.example.golfreservation.domain.entity.GolfField;
import com.example.golfreservation.domain.entity.Member;
import com.example.golfreservation.domain.entity.Reservation;
import com.example.golfreservation.domain.form.LoginForm;
import com.example.golfreservation.domain.form.MemberForm;
import com.example.golfreservation.error.DuplicateMemberId;
import com.example.golfreservation.error.NotExitMember;
import com.example.golfreservation.repository.GolfFieldRepo;
import com.example.golfreservation.repository.MemberRepo;
import com.example.golfreservation.repository.ReservationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepo memberRepo;

    private final GolfFieldRepo golfFieldRepo;

    private final ReservationRepo reservationRepo;

    public void join(MemberForm memberForm) {
        if (checkDuplicate(memberForm.getLoginId())) {
            memberRepo.save(Member.builder()
                    .loginId(memberForm.getLoginId())
                    .loginPw(memberForm.getLoginPw())
                    .name(memberForm.getName())
                    .build());
        }
    }

    @Transactional(readOnly = true)
    public Member find(Long id) {
        return memberRepo.findById(id).orElse(null);
    }

    public void editPassword(EditPassword editPassword) {
        find(editPassword.getId()).editPassword(editPassword.getLoginPw());
    }

    @Transactional(readOnly = true)
    public Boolean checkDuplicate(String loginId) {
        if (memberRepo.checkDuplicateId(loginId) != null) {
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public Long login(LoginForm loginForm) {
        return memberRepo.login(loginForm.getLoginId(), loginForm.getLoginPw());
    }

    public void changeMemberAuthority(Long loginId) {
        Member findMember = find(loginId);
        findMember.editAuthority();
    }

    public void removeMember(Long memId) {
        Member findMember = find(memId);

        List<GolfField> myGolfFieldList = findMember.getMyGolfFieldList();
        List<Reservation> reservations = findMember.getReservations();

        if (myGolfFieldList != null) {
            myGolfFieldList.forEach(golfField -> golfFieldRepo.deleteById(golfField.getId()));
        }

        if (reservations != null) {
            reservations.forEach(reservation -> {
                reservation.getFieldTime().changeAble();
                reservationRepo.deleteById(reservation.getId());
            });
        }

        memberRepo.deleteById(findMember.getId());
    }
}
