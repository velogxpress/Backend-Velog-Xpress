package com.velogexpress.repository;

import com.velogexpress.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query(value = "SELECT * FROM categories where description like lower(concat('%', :description, '%'))", nativeQuery = true)
    Page<Category> search(@Param("description") String description, Pageable pageable);

    @Query(value = "SELECT * FROM categories where part like lower(concat('%', :description, '%'))", nativeQuery = true)
    Page<Category> searchByPart(@Param("description") String description, Pageable pageable);
    Category findByDescription(String description);
}
