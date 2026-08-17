package com.velogexpress.repository;

import com.velogexpress.entity.FactureDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureDetailsRepository extends JpaRepository<FactureDetails, Long> {

    // Get all details ordered by ID desc
    Page<FactureDetails> findAllByOrderByIdDesc(Pageable pageable);

    // Get details by facture code
    @Query("SELECT d FROM FactureDetails d WHERE d.facture.code = :factureCode")
    Page<FactureDetails> findByFactureCode(@Param("factureCode") String factureCode, Pageable pageable);
    @Query(value = """
SELECT df.*
FROM facture_details df
WHERE df.facture_code = ?1
""", nativeQuery = true)
    List<FactureDetails> findByFactureCode(Long factureId);

    // Get single detail by colis
    FactureDetails findByColis(String colis);

    // Get single detail by ID (already exists in JpaRepository)
    // FactureDetails findById(Long id); // optional, JpaRepository provides findById
}

