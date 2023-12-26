package com.example.golfreservation.repository;

import com.example.golfreservation.domain.entity.FieldTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldTimeRepo extends JpaRepository<FieldTime, Long> {
}
