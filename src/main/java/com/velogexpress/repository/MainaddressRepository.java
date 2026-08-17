package com.velogexpress.repository;

import com.velogexpress.entity.Mainaddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainaddressRepository extends JpaRepository<Mainaddress, Long> {
    Page<Mainaddress>findByCity(String city, Pageable pageable);
}
