package com.velogexpress.repository;

import com.velogexpress.entity.Surcmachine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurcmachineRepository extends JpaRepository<Surcmachine, Long> {

    // Find by exact machine serial
    @Query("SELECT sm FROM Surcmachine sm JOIN sm.machine m WHERE m.serial = ?1")
    Surcmachine findByMachine(String machineSerial);

    // Search by machine serial or name (case-insensitive) with pagination
    @Query("SELECT sm FROM Surcmachine sm JOIN sm.machine m " +
            "WHERE LOWER(m.serial) LIKE LOWER(CONCAT('%', :machine, '%')) " +
            "OR LOWER(m.name) LIKE LOWER(CONCAT('%', :machine, '%'))")
    Page<Surcmachine> search(@Param("machine") String machine, Pageable pageable);
}

