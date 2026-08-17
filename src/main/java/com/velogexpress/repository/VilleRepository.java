package com.velogexpress.repository;

import com.velogexpress.entity.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {
    Optional<Ville> findByDescription(String description);

    @Query("SELECT v FROM Ville v WHERE v.region.id = :regionId")
    Page<Ville> findByRegion(@Param("regionId") Long regionId, Pageable pageable);


}
