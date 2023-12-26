package com.example.golfreservation.repository;

import com.example.golfreservation.domain.entity.GolfField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GolfFieldRepo extends JpaRepository<GolfField, Long> {
}
