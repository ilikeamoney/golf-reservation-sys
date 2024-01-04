package com.example.golfreservation.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "golf_field_id")
    private GolfField golfField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "field_time_id")
    private FieldTime fieldTime;

    @Builder
    public Reservation(GolfField golfField, Member member, FieldTime fieldTime) {
        this.golfField = golfField;
        this.member = member;
        this.fieldTime = fieldTime;
        golfField.getReservations().add(this);
        member.getReservations().add(this);
        fieldTime.setReservation(this);
    }
}
