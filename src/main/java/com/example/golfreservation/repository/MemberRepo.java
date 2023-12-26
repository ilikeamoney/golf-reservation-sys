package com.example.golfreservation.repository;

import com.example.golfreservation.domain.entity.Member;
import org.apache.el.stream.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

    @Query("SELECT m.id FROM Member m WHERE m.loginId = :loginId AND m.loginPw = :loginPw")
    Long login(@Param("loginId") String loginId, @Param("loginPw") String loginPw);

    @Query("SElECT m.id FROM Member m  WHERE m.loginId = :loginId")
    Long checkDuplicateId(@Param("loginId") String loginId);
}
