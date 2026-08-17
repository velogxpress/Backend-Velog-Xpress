package com.velogexpress.repository;

import com.velogexpress.entity.Storage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    @Query(value = "SELECT * FROM storage order by id desc", nativeQuery = true)
    List<Storage> findAllByDesc();
    @Query(value = "SELECT * FROM storage where container LIKE CONCAT('%', ?1, '%') or description LIKE CONCAT('%', ?1, '%') " +
            "or airwaybill LIKE CONCAT('%', ?1, '%') order by id desc", nativeQuery = true)
    List<Storage> findAllByDesc(String param);
    @Query(value = "SELECT * FROM storage where container=?1", nativeQuery = true)
    Storage findByContainer(String container);
}
