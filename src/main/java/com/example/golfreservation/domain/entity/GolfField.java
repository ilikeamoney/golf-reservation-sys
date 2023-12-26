package com.example.golfreservation.domain.entity;

import com.example.golfreservation.domain.edit.EditGolfField;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GolfField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "golf_field_id")
    private Long id;
    private Integer capacity;
    private Integer price;
    private String fieldName;
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "golfField")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "golfField")
    private List<FieldTime> fieldTimes;

    @Builder
    public GolfField(Integer capacity, Integer price, String fieldName, String location, Member member) {
        this.fieldName = fieldName;
        this.location = location;
        this.capacity = capacity;
        this.price = price;
        this.member = member;
        member.getMyGolfFieldList().add(this);
    }

    public void editField(EditGolfField editGolfField) {
        this.fieldName = editGolfField.getFiledName() != null ? editGolfField.getFiledName() : this.fieldName;
        this.location = editGolfField.getLocation() != null ? editGolfField.getLocation() : this.location;
        this.price = editGolfField.getPrice() != null ? editGolfField.getPrice() : this.price;
        this.capacity = editGolfField.getCapacity() != null ? editGolfField.getCapacity() : this.capacity;
    }
}
