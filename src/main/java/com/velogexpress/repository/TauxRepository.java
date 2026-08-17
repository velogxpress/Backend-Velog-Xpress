package com.velogexpress.repository;

import com.velogexpress.entity.Taux;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TauxRepository extends JpaRepository<Taux,Long> {
    @Query(value = "SELECT * FROM taux where LOWER(devise) LIKE LOWER(CONCAT('%', :param, '%')) or LOWER(symbole) LIKE LOWER(CONCAT('%', :param, '%'))", nativeQuery = true)
    Page<Taux> findByDevise(@Param("param") String description, Pageable pageable);
    Taux findByDevise(String description);
    @Query(value = "SELECT * FROM taux where devise=?1", nativeQuery = true)
    Page<Taux> findByAllDevise(String description, Pageable pageable);
}
