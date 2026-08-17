package com.velogexpress.repository;

import com.velogexpress.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByShiporder(String upc);
    @Query(value = "SELECT * FROM ship_order order by id desc", nativeQuery = true)
    Page<Order> findByShiporderList(Pageable pageable);
    @Query(value = "SELECT * FROM ship_order where ship_order= ?1", nativeQuery = true)
    Page<Order> findByShiporderList(String upc, Pageable pageable);

    @Query(value = """
    SELECT COALESCE(SUM(qty_colis), 0)
    FROM ship_order
    WHERE YEAR(STR_TO_DATE(created_at, '%d-%m-%Y')) = :year
""", nativeQuery = true)
    Long countColis(@Param("year") int year);



}
