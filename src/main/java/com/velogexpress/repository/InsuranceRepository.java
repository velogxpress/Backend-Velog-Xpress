package com.velogexpress.repository;

import com.velogexpress.entity.Insurance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance,Long> {
   Page<Insurance> findByAmount(Double id, Pageable pageable);
}
