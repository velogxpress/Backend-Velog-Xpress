package com.velogexpress.repository;

import com.velogexpress.entity.Historique;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique,Long> {
    @Query(value = " SELECT hl.id,hl.login_date,hl.logout_date,hl.place,hl.user_id,cl.full_name,cl.email,cl.usercode FROM historique_connexion hl" +
            " LEFT JOIN client_register cl ON cl.id=hl.user_id where cl.usercode= ?1 and hl.logout_date='N/A' order by id desc", nativeQuery = true)
    Historique getHistoriqueLogin(String user);

    @Query(value = " SELECT hl.id,hl.login_date,hl.logout_date,hl.place,hl.user_id,cl.full_name,cl.email,cl.usercode FROM historique_connexion hl" +
            " LEFT JOIN client_register cl ON cl.id=hl.user_id where cl.usercode like lower(concat('%', :user, '%')) or " +
            "cl.full_name like lower(concat('%', :user, '%')) or cl.email like lower(concat('%', :user, '%')) order by id desc", nativeQuery = true)
    Page<Historique> getHistoriqueLoginList(@Param("user") String user, Pageable pageable);

    @Query(value = " SELECT hl.id,hl.login_date,hl.logout_date,hl.place,hl.user_id,cl.full_name,cl.email,cl.usercode FROM historique_connexion hl" +
            " LEFT JOIN client_register cl ON cl.id=hl.user_id order by id desc", nativeQuery = true)
    Page<Historique> getAllHistoriqueLoginList(Pageable pageable);
}
