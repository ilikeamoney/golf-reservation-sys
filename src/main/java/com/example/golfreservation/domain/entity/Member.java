package com.example.golfreservation.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String loginId;
    private String loginPw;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<GolfField> myGolfFieldList;

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations;

    @Builder
    public Member(String loginId, String loginPw, String name) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.authority = Authority.USER;
    }

    public void editPassword(String loginPw) {
        this.loginPw = loginPw != null ? loginPw : this.getLoginPw();
    }

    public void editAuthority() {
        this.authority = Authority.BUSINESS;
    }

}
