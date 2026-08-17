package com.velogexpress.repository;

import com.velogexpress.entity.Amnisty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AmnistyRepository extends JpaRepository<Amnisty, Long> {
    @Query(value = " SELECT * FROM amnisty where tracking LIKE CONCAT('%', :param, '%')", nativeQuery = true)
    Page<Amnisty> search(String param, Pageable pageable);

    Amnisty findByTracking(String upc);

    @Query(value = " SELECT * FROM amnisty order by id desc", nativeQuery = true)
    Page<Amnisty> findAllByOrderIdDesc(Pageable pageable);

    @Query(value = " SELECT * FROM amnisty where name=?1 and telephone=?2", nativeQuery = true)
    List<Amnisty> findAllAmnistyByClient(String name,String telephone);

    @Query(value = " SELECT * FROM amnisty where tracking LIKE CONCAT('%', :param, '%')", nativeQuery = true)
    List<Amnisty> searchAmnisty(String param);
}
