package com.velogexpress.repository;

import com.velogexpress.entity.Machine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    Optional<Machine> findBySerial(String serial);

    @Query(value = """
        SELECT * FROM machine 
        WHERE LOWER(machine_serial) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(machine_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(machine_marque) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """,
            countQuery = """
        SELECT COUNT(*) FROM machine 
        WHERE LOWER(machine_serial) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(machine_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(machine_marque) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """,
            nativeQuery = true)
    Page<Machine> search(@Param("keyword") String keyword, Pageable pageable);
}
