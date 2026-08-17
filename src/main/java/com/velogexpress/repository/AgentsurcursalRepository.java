package com.velogexpress.repository;

import com.velogexpress.entity.Agentsurcursal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentsurcursalRepository extends JpaRepository<Agentsurcursal,Long> {
    @Query(value = "SELECT c.id as client_code,c.full_name,c.phone,c.role,c.status,c.usercode,c.ville_id,c.address," +
            "ass.id,ass.client_id,ass.surcursal_id FROM agent_vs_surcursal ass join client_register c on c.id=ass.client_id " +
            "where c.usercode= ?1", nativeQuery = true)
    Agentsurcursal findByUserCode(String userCode);

    @Query(
            value = """
        SELECT 
            c.id AS clientCode,
            c.full_name AS fullName,
            c.phone AS phone,
            c.role AS role,
            c.status AS status,
            c.usercode AS usercode,
            c.ville_id AS villeId,
            c.address AS address,
            c.email AS email,
            ass.id,
            ass.client_id ,
            ass.surcursal_id ,
            s.surc_name AS surcursalName
        FROM agent_vs_surcursal ass
        JOIN surcursal s ON s.id = ass.surcursal_id
        JOIN client_register c ON c.id = ass.client_id
        WHERE (
            LOWER(c.usercode) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(c.full_name) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(c.address) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(s.surc_name) LIKE LOWER(CONCAT('%', :userCode, '%'))
        )
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM agent_vs_surcursal ass
        JOIN surcursal s ON s.id = ass.surcursal_id
        JOIN client_register c ON c.id = ass.client_id
        WHERE (
            LOWER(c.usercode) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(c.full_name) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(c.address) LIKE LOWER(CONCAT('%', :userCode, '%'))
            OR LOWER(s.surc_name) LIKE LOWER(CONCAT('%', :userCode, '%'))
        )
        """,
            nativeQuery = true
    )
    Page<Agentsurcursal> search(@Param("userCode") String userCode, Pageable pageable);


}
