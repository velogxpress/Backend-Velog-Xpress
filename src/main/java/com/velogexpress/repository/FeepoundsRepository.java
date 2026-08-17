package com.velogexpress.repository;

import com.velogexpress.entity.Feepounds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeepoundsRepository extends JpaRepository<Feepounds,Long> {
    Page<Feepounds> findByAmount(Double id, Pageable pageable);
}
