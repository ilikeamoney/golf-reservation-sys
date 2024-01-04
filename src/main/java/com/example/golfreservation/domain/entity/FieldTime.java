package com.example.golfreservation.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_time_id")
    private Long id;

    private LocalDateTime localDateTime;

    private Boolean checkState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "golf_field_id")
    private GolfField golfField;

    @OneToOne(mappedBy = "fieldTime")
    private Reservation reservation;

    @Builder
    public FieldTime(LocalDateTime localDateTime, GolfField golfField) {
        this.localDateTime = localDateTime;
        this.golfField = golfField;
        this.checkState = false;
        golfField.getFieldTimes().add(this);
    }

    public void editTime(LocalDateTime editTime) {
        this.localDateTime = editTime != null ? editTime : this.localDateTime;
    }

    public void changeDisable() {
        this.checkState = true;
    }

    public void changeAble() {
        this.checkState = false;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
