package com.velogexpress.repository;

import com.velogexpress.entity.Surcursal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurcursalRepository extends JpaRepository<Surcursal, Long> {
    Surcursal findByName(String surcursalName);

    @Query("SELECT s FROM Surcursal s WHERE lower(s.ville.description) = lower(?1)")
    Page<Surcursal> findByVille(String ville, Pageable pageable);

    @Query("""
    SELECT s FROM Surcursal s
    WHERE LOWER(s.ville.description) LIKE LOWER(CONCAT('%', :ville, '%'))
       OR LOWER(s.name) LIKE LOWER(CONCAT('%', :ville, '%'))
       OR s.phone LIKE CONCAT('%', :ville, '%')
""")
    Page<Surcursal> search(@Param("ville") String ville, Pageable pageable);

}
