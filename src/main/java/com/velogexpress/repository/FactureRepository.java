package com.velogexpress.repository;

import com.velogexpress.entity.Facture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {
    @Query(value = "SELECT * FROM facture GROUP BY id ORDER BY id DESC",nativeQuery = true)
    Page<Facture> findAllByDesc(Pageable pageable);
    Facture findByCode(String code);

    // Search facture by code or date
    @Query(value = "SELECT f.id,f.amount,f.assurance,f.client,f.client_phone,f.facture_code,f.date,f.discount,f.status,f.subtotal,f.tarif," +
            "f.order_id,f.user_id,f.surcursal_id,f.destination,sh.ship_order,f.balance,f.effectif FROM facture f  left join ship_order sh on sh.id=f.order_id " +
            " WHERE f.facture_code LIKE LOWER(CONCAT('%', :code, '%')) OR f.date LIKE CONCAT('%', :code, '%') or LOWER(f.client) LIKE LOWER(CONCAT('%', :code, '%'))" +
            " or f.client_phone LIKE LOWER(CONCAT('%', :code, '%')) or sh.ship_order LIKE LOWER(CONCAT('%', :code, '%')) ORDER BY id DESC",nativeQuery = true)
    Page<Facture> searchFacture(@Param("code") String code, Pageable pageable);

    // Get all factures ordered by ID desc
    Page<Facture> findAllByOrderByIdDesc(Pageable pageable);

    @Query(value = "SELECT f.id,f.amount,f.assurance,f.client,f.client_phone,f.facture_code,f.date,f.discount,f.status,f.subtotal,f.tarif," +
            "f.order_id,f.user_id,f.surcursal_id,f.destination,df.id as code,df.colis,df.description,df.fee,df.fixe,df.poids,df.subtotal as total,df.category_id,df.facture_code as codefacture" +
            " FROM facture f left join facture_details df on df.facture_code=f.id where f.client_phone=?1 and f.order_id=?2 order by id desc", nativeQuery = true)
    Page<Facture> findFactureWithDetails(String client,Long order, Pageable pageable);

    @Query(value = "SELECT id,date,sum(amount) as amount,assurance,balance,client,client_phone,facture_code" +
            ",discount,effectif,status,subtotal,order_id,surcursal_id,destination,user_id,tarif  from facture where status='Payé' and date=:date", nativeQuery = true)
    Facture sumAmountToday(@Param("date") String date);
    @Query(value = "SELECT f.id,sum(f.amount) as amount,sum(f.assurance) as assurance,f.client,f.client_phone,f.facture_code,f.date,sum(f.discount) as discount," +
            "f.status,sum(f.subtotal) as subtotal,sum(f.tarif) as tarif,sum(f.balance) as balance,sum(f.effectif) as effectif," +
            "f.order_id,f.user_id,f.surcursal_id,f.destination FROM facture f where f.order_id=?1 group by surcursal_id order by id desc", nativeQuery = true)
    Page<Facture> getFactureByOrder(Long order, Pageable pageable);
    @Query(value = "SELECT f.id,sum(f.amount) as amount,sum(f.assurance) as assurance,f.client,f.client_phone,f.facture_code,f.date," +
            "sum(f.discount) as discount,f.status,sum(f.subtotal) as subtotal,sum(f.tarif) as tarif,sum(f.balance) as balance,sum(f.effectif) as effectif," +
            "f.order_id,f.user_id,f.surcursal_id,f.destination,s.id as surcursal,ag.id as agent_id,ag.surcursal_id as surcursalcode FROM facture f " +
            "left join agent_vs_surcursal ag on ag.id =f.surcursal_id left join surcursal s on s.id=ag.surcursal_id \n" +
            "where f.order_id=?1 and ag.surcursal_id=?2 " +
            "group by f.surcursal_id order by id desc", nativeQuery = true)
    Page<Facture> getFactureBySurcursal(Long order, Long surcursal, Pageable pageable);
    @Query(value = "SELECT f.id,f.date,sum(f.amount) as amount,f.assurance,f.balance,f.client,f.client_phone,f.facture_code" +
            ",f.discount,f.effectif,f.status,sum(f.subtotal) as subtotal,f.order_id,f.surcursal_id,f.destination,f.user_id,f.tarif,s.id as code  from facture f " +
            " left join agent_vs_surcursal ag on ag.id=f.surcursal_id left join surcursal s on s.id=ag.surcursal_id " +
            " where f.status='Payé' and f.date=?1  and s.id=?2", nativeQuery = true)
    Facture sumAmountTodayFromMyCity(String date, Long id);
}
