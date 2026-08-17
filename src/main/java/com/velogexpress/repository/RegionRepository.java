package com.velogexpress.repository;

import com.velogexpress.entity.Region;
import com.velogexpress.entity.Taux;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
      Page<Region> findByDescriptionContainingIgnoreCase(String searchTerm, Pageable pageable);
      Region findByDescription(String description);
      @Query(value = "SELECT * FROM region", nativeQuery = true)
      Page<Region> findByAllRegion(Pageable pageable);
}
