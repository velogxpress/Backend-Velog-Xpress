package com.velogexpress.repository;

import com.velogexpress.entity.StorageDetails;
import com.velogexpress.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query(value = "SELECT * FROM store WHERE orderdetails_id=?1", nativeQuery = true)
    Store findByOrderDetails(Long orderdetailsID);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM store WHERE orderdetails_id=?1", nativeQuery = true)
    void deleteByOrderDetails(Long orderdetailsID);
}
