package com.velogexpress.repository;

import com.velogexpress.entity.Specialfee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialfeeRepository extends JpaRepository<Specialfee,Long> {

    Page<Specialfee> findByAmount(Double id, Pageable pageable);
}
