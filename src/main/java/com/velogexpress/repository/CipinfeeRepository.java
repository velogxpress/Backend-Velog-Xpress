package com.velogexpress.repository;

import com.velogexpress.entity.Cipinfee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CipinfeeRepository extends JpaRepository<Cipinfee,Long> {

    @Query(value = "SELECT * FROM city_vs_fees cf WHERE cf.ville_id = ?1", nativeQuery = true)
    Cipinfee findByCityID(Long cityID);


    @Query(
            value = "SELECT cf.* FROM city_vs_fees cf " +
                    "INNER JOIN ville v ON v.id = cf.ville_id " +
                    "WHERE LOWER(v.description) LIKE LOWER(CONCAT('%', :cityDesc, '%'))",
            countQuery = "SELECT COUNT(*) FROM city_vs_fees cf " +
                    "INNER JOIN ville v ON v.id = cf.ville_id " +
                    "WHERE LOWER(v.description) LIKE LOWER(CONCAT('%', :cityDesc, '%'))",
            nativeQuery = true
    )
    Page<Cipinfee> findByCityDesc(
            @Param("cityDesc") String cityDesc,
            Pageable pageable
    );



}

