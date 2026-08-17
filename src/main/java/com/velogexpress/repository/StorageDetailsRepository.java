package com.velogexpress.repository;

import com.velogexpress.entity.Storage;
import com.velogexpress.entity.StorageDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StorageDetailsRepository extends JpaRepository<StorageDetails, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM storage_details WHERE orderdetails_id=?1", nativeQuery = true)
    void deleteByOrderDetails(Long orderdetailsID);

    @Query(value = "SELECT DISTINCT sd.id, s.container, s.description, s.airwaybill, s.order_id, o.ship_order, sd.orderdetails_id," +
            "v.description AS ville, od.rec_name, od.rec_phone,sd.storage_id,od.pounds,od.category_id,od.upc_colis " +
            " FROM storage_details sd " +
            " LEFT JOIN storage s ON s.id = sd.storage_id " +
            " LEFT JOIN ship_order o ON o.id = s.order_id " +
            " LEFT JOIN order_details od ON sd.orderdetails_id = od.id " +
            " LEFT JOIN city_vs_fees cf ON cf.id = od.city_fees " +
            " LEFT JOIN ville v ON v.id = cf.ville_id where o.ship_order=?1  order by id desc", nativeQuery = true)
    List<StorageDetails> findAllByDesc(String order);

    @Query(value = "SELECT DISTINCT sd.id, s.container, s.description, s.airwaybill, s.order_id, o.ship_order, sd.orderdetails_id," +
            "v.description AS ville, od.rec_name, od.rec_phone,sd.storage_id,od.pounds,od.category_id,od.upc_colis  " +
            " FROM storage_details sd " +
            " LEFT JOIN storage s ON s.id = sd.storage_id " +
            " LEFT JOIN ship_order o ON o.id = s.order_id " +
            " LEFT JOIN order_details od ON sd.orderdetails_id = od.id " +
            " LEFT JOIN city_vs_fees cf ON cf.id = od.city_fees " +
            " LEFT JOIN ville v ON v.id = cf.ville_id " +
            " WHERE o.ship_order=?1 AND s.container LIKE CONCAT('%', ?2, '%') OR o.ship_order LIKE CONCAT('%', ?2, '%') " +
            " OR od.rec_name LIKE CONCAT('%', ?2, '%') OR od.rec_phone LIKE CONCAT('%', ?2, '%') " +
            " OR od.upc_colis LIKE CONCAT('%', ?2, '%') " +
            "ORDER BY sd.id DESC",
            nativeQuery = true)
    List<StorageDetails> searchStorage(String order,String container);

    @Query(value = """

            SELECT
                        sd.id,s.container AS container,
                        MAX(s.description) AS description,
                        MAX(s.airwaybill) AS airwaybill,sd.storage_id,
                        COUNT(DISTINCT sd.orderdetails_id) AS orderdetails_id,
                        SUM(od.pounds) AS pounds
                    FROM storage_details sd
                    JOIN storage s ON s.id = sd.storage_id
                    JOIN order_details od ON od.id = sd.orderdetails_id
                    JOIN ship_order o ON o.id = s.order_id
                    
                    WHERE o.ship_order = ?1
                    
                    GROUP BY s.container""", nativeQuery = true)
    List<StorageDetails> findByContainer(String order);
}
