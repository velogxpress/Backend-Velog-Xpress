package com.velogexpress.repository;

import com.velogexpress.entity.Recovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecoveryRepository extends JpaRepository<Recovery, Long> {
    @Query("SELECT r FROM Recovery r WHERE r.email = :email AND r.status = 'N/V'")
    Optional<Recovery> findPendingByEmail(@Param("email") String email);
    @Query("SELECT MAX(r.id) FROM Recovery r WHERE r.email = :email AND r.status = 'N/V'")
    Long findMaxIDRecovery(@Param("email") String email);

    @Query("SELECT r FROM Recovery r WHERE r.id = :id")
    Recovery findPingByEmail(@Param("id") Long id);


    Recovery findByResetToken(String token);
}
