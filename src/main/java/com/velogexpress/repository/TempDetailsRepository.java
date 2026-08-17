package com.velogexpress.repository;

import com.velogexpress.entity.TempDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TempDetailsRepository extends JpaRepository<TempDetails,Long> {
    @Query(value = "SELECT * FROM temp_facture_details where client=?1 order by id desc", nativeQuery = true)
    Page<TempDetails> searchAllTMP(String client, Pageable pageable);
    @Query(value = "SELECT * FROM temp_facture_details  where colis=?1", nativeQuery = true)
    TempDetails getFactureDetailsTMP(String factureID);
    @Query(value = "SELECT * FROM temp_facture_details  where id=?1", nativeQuery = true)
    TempDetails getFactureByIDTMP(Long colis);
    @Query(value = "SELECT * FROM temp_facture_details  where client=?1", nativeQuery = true)
    Page<TempDetails> getFactureByClientTMP(String client, Pageable pageable);
}
