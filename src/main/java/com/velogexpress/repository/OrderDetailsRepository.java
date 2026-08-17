package com.velogexpress.repository;

import com.velogexpress.entity.Facture;
import com.velogexpress.entity.OrderDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.net.http.HttpHeaders;
import java.util.List;


public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
    @Query(value = "SELECT * FROM order_details where upc_colis= ?1", nativeQuery = true)
    OrderDetails findByUpc(String upc);

    @Query(value = "SELECT * FROM order_details where order_id= ?1", nativeQuery = true)
    Page<OrderDetails> findByShip(Long orderCode, Pageable pageable);
    @Query(value = "SELECT * FROM order_details where order_id= ?1", nativeQuery = true)
    List<OrderDetails> findByShipID(Long orderCode);

    @Query(value = "SELECT DISTINCT * FROM order_details where order_id= ?1", nativeQuery = true)
    Page<OrderDetails> findDistinct(Long orderCode, Pageable pageable);

    @Query(value = "SELECT * FROM order_details where order_id= ?1", nativeQuery = true)
    Page<OrderDetails> findByShipID(Long orderCode, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone," +
            " od.rec_email,od.rec_name,od.rec_phone,od.condition,od.price,od.tracking,od.douane,od.created_at," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id  FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " where sh.ship_order= ?1 order by id desc", nativeQuery = true)
    Page<OrderDetails> findShipID(String orderCode, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone," +
            " od.rec_email,od.rec_name,od.rec_phone,od.condition,od.price,od.tracking,od.douane,od.created_at," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " where sh.ship_order= ?1 order by id desc", nativeQuery = true)
    List<OrderDetails> finddShipID(String orderCode);

    @Query(value = "SELECT * FROM order_details where order_id= ?1 and rec_phone=?2", nativeQuery = true)
    Page<OrderDetails> findDetailsByParameter(Long orderCode,String clientCode, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.category_id," +
            " od.city_fees,od.client_id,od.order_id,cr.usercode,sh.ship_order ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr ON cr.id=od.client_id " +
            " left join ship_order sh on sh.id=od.order_id where od.status='Commande prête à délivrer.' and cr.usercode= ?1 and sh.ship_order= ?2", nativeQuery = true)
    Page<OrderDetails> findClientIDForFacture(String clientCode,String shipCode, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.category_id," +
            " od.city_fees,od.client_id,od.order_id,cr.usercode,sh.ship_order ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr ON cr.id=od.client_id " +
            " left join ship_order sh on sh.id=od.order_id where od.rec_phone= ?1 and sh.ship_order= ?2", nativeQuery = true)
    Page<OrderDetails> findQuickFacture(String clientCode,String shipCode, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.category_id," +
            " od.city_fees,od.client_id,od.order_id,cr.usercode ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr ON cr.id=od.client_id " +
            " where od.status='Commande prête à délivrer.' and (cr.usercode= ?1 or od.rec_phone= ?1) group by order_id order by id desc", nativeQuery = true)
    Page<OrderDetails> findClientIDForFacture(String clientCode, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.category_id," +
            " od.city_fees,od.client_id,od.order_id,cr.usercode,sh.ship_order ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.codition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr ON cr.id=od.client_id " +
            " left join ship_order sh on sh.id=od.order_id where od.rec_phone= ?1 and sh.ship_order= ?2", nativeQuery = true)
    Page<OrderDetails> searchDetailsFacture(String clientCode,String shipCode, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.category_id," +
            " od.city_fees,od.client_id,od.order_id,cr.usercode,sh.ship_order ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr ON cr.id=od.client_id " +
            " left join ship_order sh on sh.id=od.order_id where  od.upc_colis= ?1" +
            " group by order_id order by id desc", nativeQuery = true)
    Page<OrderDetails> findColis(String orderCode, Pageable pageable);

    @Query(value = " SELECT count(od.id) AS id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.category_id," +
            "od.city_fees,od.client_id,od.order_id,cr.usercode,sh.ship_order,v.description ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            "od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr " +
            "ON cr.id=od.client_id left join ship_order sh on sh.id=od.order_id LEFT JOIN city_vs_fees cf ON cf.id=od.city_fees " +
            "LEFT JOIN ville v ON v.id=cf.ville_id GROUP BY DESCRIPTION", nativeQuery = true)
    Page<OrderDetails> countShipping(Pageable pageable);

    @Query(value = "SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,count(od.upc_colis) as upc_colis,od.category_id," +
            "od.city_fees,od.client_id,od.order_id,cr.usercode ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            "od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr " +
            "ON cr.id=od.client_id WHERE cr.usercode=?1 AND od.status='Commande délivré.' GROUP BY usercode", nativeQuery = true)
    Page<OrderDetails> countDelevredColis(String code, Pageable pageable);

    @Query(value = "SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,count(od.upc_colis) as upc_colis,od.category_id," +
            "od.city_fees,od.client_id,od.order_id,cr.usercode ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            "od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr " +
            "ON cr.id=od.client_id WHERE cr.usercode=?1 AND od.status='Commande expédiée.' GROUP BY usercode", nativeQuery = true)
    Page<OrderDetails> countShippedColis(String code, Pageable pageable);

    @Query(value = "SELECT od.id,od.cash_decision,od.delivery_date,od.pounds,od.status,od.sub_total,count(od.upc_colis) as upc_colis,od.category_id," +
            "od.city_fees,od.client_id,od.order_id,cr.usercode ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            "od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr " +
            "ON cr.id=od.client_id WHERE cr.usercode=?1 AND od.status='Commande prête à délivrer.' GROUP BY usercode", nativeQuery = true)
    Page<OrderDetails> countReadyColis(String code, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.category_id," +
            " od.city_fees,od.client_id,od.order_id,cr.usercode,sh.ship_order ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr ON cr.id=od.client_id " +
            " left join ship_order sh on sh.id=od.order_id where  cr.usercode= ?1 order by id desc", nativeQuery = true)
    Page<OrderDetails> getOrderDetailByClient(String client, Pageable pageable);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.category_id," +
            " od.city_fees,od.client_id,od.order_id,cr.usercode,sh.ship_order ,od.exp_email,od.exp_name,od.exp_phone,od.tracking,od.douane,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.colis_type,od.condition,od.price,od.colis_image,od.note,od.user_id   FROM order_details od LEFT JOIN client_register cr ON cr.id=od.client_id " +
            " left join ship_order sh on sh.id=od.order_id where  cr.usercode= ?1 AND (\n" +
            "    sh.ship_order LIKE CONCAT('%', ?2, '%')\n" +
            "    OR od.upc_colis LIKE CONCAT('%', ?2, '%')\n" +
            "    OR od.rec_name LIKE CONCAT('%', ?2, '%')\n" +
            "    OR od.rec_phone LIKE CONCAT('%', ?2, '%')\n" +
            "    OR od.rec_email LIKE CONCAT('%', ?2, '%')\n" +
            "    OR od.exp_name LIKE CONCAT('%', ?2, '%')\n" +
            "    OR od.exp_phone LIKE CONCAT('%', ?2, '%')\n" +
            "    OR od.tracking LIKE CONCAT('%', ?2, '%')\n" +
            ") order by id desc", nativeQuery = true)
    Page<OrderDetails> getOrderDetailSearchByClient(String client,String search, Pageable pageable);
    @Query(value = """
  SELECT     od.id,
              od.delivery_date,
              od.pounds,
              od.status,
              od.sub_total,
              od.upc_colis,
              od.category_id,
              od.city_fees,
              od.client_id,
              od.order_id,
              cr.usercode,
              sh.ship_order,
              od.exp_email,
              od.exp_name,
              od.exp_phone,
              od.rec_email,
              od.rec_name,
              od.rec_phone,
              od.colis_type,
              od.condition,
              od.price,od.tracking,od.douane,od.created_at,od.colis_image,od.note,od.user_id 
          FROM order_details od
          Left join client_register cr on cr.id =od.client_id
          Left join ship_order sh on sh.id =od.order_id
  WHERE cr.usercode = ?1 AND sh.ship_order=?2""", nativeQuery = true)
    List<OrderDetails> getOrderDetailFactureByClient(String client, String search);



    @Query(value = """
SELECT 
    od.id,
    od.delivery_date,
    od.pounds,
    od.status,
    od.sub_total,
    od.upc_colis,
    od.category_id,
    od.city_fees,
    od.client_id,
    od.order_id,
    cr.usercode,
    sh.ship_order,
    od.exp_email,
    od.exp_name,
    od.exp_phone,
    od.rec_email,
    od.rec_name,
    od.rec_phone,
    od.colis_type,
    od.condition,
    od.price,od.tracking,od.douane,od.created_at,od.colis_image,od.note,od.user_id  
FROM order_details od
LEFT JOIN client_register cr ON cr.id = od.client_id
LEFT JOIN ship_order sh ON sh.id = od.order_id
WHERE sh.ship_order = ?1
AND (
    sh.ship_order LIKE CONCAT('%', ?2, '%')
    OR od.upc_colis LIKE CONCAT('%', ?2, '%')
    OR od.rec_name LIKE CONCAT('%', ?2, '%')
    OR od.rec_phone LIKE CONCAT('%', ?2, '%')
    OR od.rec_email LIKE CONCAT('%', ?2, '%')
    OR od.exp_name LIKE CONCAT('%', ?2, '%')
    OR od.exp_phone LIKE CONCAT('%', ?2, '%')
)
""", nativeQuery = true)
    Page<OrderDetails> searchOrderDetailByClient(
            String shipOrder,
            String search,
            Pageable pageable
    );
    @Query(value = """
SELECT 
    od.id,
    od.delivery_date,
    od.pounds,
    od.status,
    od.sub_total,
    od.upc_colis,
    od.category_id,
    od.city_fees,
    od.client_id,
    od.order_id,
    cr.usercode,
    sh.ship_order,
    od.exp_email,
    od.exp_name,
    od.exp_phone,
    od.rec_email,
    od.rec_name,
    od.rec_phone,
    od.colis_type,
    od.condition,
    od.price,od.tracking,od.douane,od.created_at,od.colis_image,od.note,od.user_id  
FROM order_details od
LEFT JOIN client_register cr ON cr.id = od.client_id
LEFT JOIN ship_order sh ON sh.id = od.order_id
WHERE sh.ship_order = ?1
AND (
    sh.ship_order LIKE CONCAT('%', ?2, '%')
    OR od.upc_colis LIKE CONCAT('%', ?2, '%')
    OR od.rec_name LIKE CONCAT('%', ?2, '%')
    OR od.rec_phone LIKE CONCAT('%', ?2, '%')
    OR od.rec_email LIKE CONCAT('%', ?2, '%')
    OR od.exp_name LIKE CONCAT('%', ?2, '%')
    OR od.exp_phone LIKE CONCAT('%', ?2, '%')
)
""", nativeQuery = true)
    List<OrderDetails> searchsOrderDetailByClient(
            String shipOrder,
            String search);
    @Query(value = """
SELECT 
    od.id,
    od.delivery_date,
    od.pounds,
    od.status,
    od.sub_total,
    od.upc_colis,
    od.category_id,
    od.city_fees,
    od.client_id,
    od.order_id,
    cr.usercode,
    sh.ship_order,
    od.exp_email,
    od.exp_name,
    od.exp_phone,
    od.rec_email,
    od.rec_name,
    od.rec_phone,
    od.colis_type,
    od.condition,
    od.price,od.tracking,od.douane,od.created_at,od.colis_image,od.note,od.user_id  
FROM order_details od
LEFT JOIN client_register cr ON cr.id = od.client_id
LEFT JOIN ship_order sh ON sh.id = od.order_id
WHERE od.upc_colis = ?1
""", nativeQuery = true)
    OrderDetails searchColis(String search);

    @Query(value = """
SELECT 
    od.id,
    od.delivery_date,
    od.pounds,
    od.status,
    od.sub_total,
    od.upc_colis,
    od.category_id,
    od.city_fees,
    od.client_id,
    od.order_id,
    cr.usercode,
    sh.ship_order,
    od.exp_email,
    od.exp_name,
    od.exp_phone,
    od.rec_email,
    od.rec_name,
    od.rec_phone,
    od.colis_type,
    od.condition,
    od.price,od.tracking,od.douane,od.created_at,od.colis_image,od.note,od.user_id  
FROM order_details od
LEFT JOIN client_register cr ON cr.id = od.client_id
LEFT JOIN ship_order sh ON sh.id = od.order_id
WHERE sh.ship_order = ?1
""", nativeQuery = true)
    List<OrderDetails> updateDetails(String search);

    @Query(value = """
SELECT 
    od.id,
    od.delivery_date,
    od.pounds,
    od.status,
    od.sub_total,
    od.upc_colis,
    od.category_id,
    od.city_fees,
    od.client_id,
    od.order_id,
    cr.usercode,
    sh.ship_order,
    od.exp_email,
    od.exp_name,
    od.exp_phone,
    od.rec_email,
    od.rec_name,
    od.rec_phone,
    od.colis_type,
    od.condition,
    od.price,od.tracking,od.douane,od.created_at,od.colis_image,od.note,od.user_id 
FROM order_details od
LEFT JOIN client_register cr ON cr.id = od.client_id
LEFT JOIN ship_order sh ON sh.id = od.order_id
WHERE od.upc_colis = ?1
""", nativeQuery = true)
    OrderDetails updateDetailsStatus(String search);


    @Query(value = " SELECT * FROM order_details where  exp_phone= ?1 group by exp_phone", nativeQuery = true)
    OrderDetails findExpediteur(String phone);
    @Query(value = " SELECT * FROM order_details where  rec_phone= ?1 group by rec_phone", nativeQuery = true)
    OrderDetails findReceiver(String phone);

    @Query(value = " SELECT * FROM order_details where  order_id=?1 and rec_phone= ?2 group by rec_phone", nativeQuery = true)
    OrderDetails findReceivers(Long id,String upc);

    @Query(value = " SELECT * FROM order_details where  order_id=?1 and rec_name= ?2 and rec_phone=?3", nativeQuery = true)
    List<OrderDetails> findOrderDetails(Long id,String name,String phone);

    @Query(value = " SELECT * FROM order_details where  order_id=?1  group by rec_phone", nativeQuery = true)
    List<OrderDetails> findReceiverInOrderDetails(Long order);

    @Query(value = " SELECT * FROM order_details where  order_id=?1 AND (rec_name LIKE CONCAT('%', ?2, '%') " +
            " OR rec_phone LIKE CONCAT('%', ?2, '%') OR rec_email LIKE CONCAT('%', ?2, '%')) group by rec_phone", nativeQuery = true)
    List<OrderDetails> searchReceiverInOrderDetails(Long order,String search);

    @Query(value = " SELECT * FROM order_details where  order_id=?1 AND rec_phone=?2", nativeQuery = true)
    List<OrderDetails> searchReceiverInOrderDetailsForFacture(Long order,String search);

    @Query(value = " SELECT * FROM order_details where  order_id=?1 AND rec_phone=?2", nativeQuery = true)
    List<OrderDetails> getReceiverInOrderDetails(Long order,String search);
    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone," +
            " od.rec_email,od.rec_name,od.rec_phone,od.codition,od.price,od.tracking,od.douane,od.created_at," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " where sh.ship_order= ?1 group by rec_phone order by id desc", nativeQuery = true)
    Page<OrderDetails> findShipIDGroup(String up, Pageable pageable);
    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.created_at,od.price,od.condition," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id  FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " where sh.ship_order= ?1 and od.rec_phone=?2", nativeQuery = true)
    Page<OrderDetails> findByOrderClient(String order, String client, Pageable pageable);
    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.created_at,od.price,od.condition," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id  FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " where sh.ship_order= ?1 group by city_fees", nativeQuery = true)
    Page<OrderDetails> searchCity(String order, Pageable pageable);
    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total,od.upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.created_at,od.price,od.condition,cf.ville_id, " +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where sh.ship_order= ?1 and cf.ville_id=?2", nativeQuery = true)
    List<OrderDetails> findShipIDCity(String orderCode,Long id);

    @Query(value = " SELECT od.id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,sum(od.price) as price,od.condition,cf.ville_id, " +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where sh.ship_order= ?1 and cf.ville_id=?2 group by rec_name", nativeQuery = true)
    List<OrderDetails> countDetailsForFacture(String orderCode,Long id);
    @Query(value = " SELECT od.id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,sum(od.price) as price,od.condition,cf.ville_id, " +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where sh.ship_order= ?1 and cf.ville_id=?2 " +
            " AND (od.upc_colis LIKE CONCAT('%', ?3, '%')" +
            "    OR od.rec_name LIKE CONCAT('%', ?3, '%')" +
            "    OR od.rec_phone LIKE CONCAT('%', ?3, '%')" +
            "    OR od.rec_email LIKE CONCAT('%', ?3, '%')" +
            "    OR od.exp_name LIKE CONCAT('%', ?3, '%')" +
            "    OR od.exp_phone LIKE CONCAT('%', ?3, '%')" +
            ")group by rec_name", nativeQuery = true)
    List<OrderDetails> countDetailsForsearch(String orderCode,Long id,String search);

    @Query(value = "SELECT * FROM order_details group by rec_phone", nativeQuery = true)
    Page<OrderDetails> findAllDesc(Pageable pageable);

    @Query(value = "SELECT * FROM order_details where rec_name LIKE CONCAT('%', :param, '%') " +
            "or rec_phone LIKE CONCAT('%', :param, '%') or rec_email LIKE CONCAT('%', :param, '%') group by rec_phone", nativeQuery = true)
    Page<OrderDetails> findForColis(@Param("param") String param, Pageable pageable);
    @Query(value = "SELECT od.id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            " count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,sum(od.price) as price,od.condition,cf.ville_id," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id   FROM order_details od " +
            " LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where od.condition='Due'", nativeQuery = true)
    OrderDetails sumAmountFacture();

    @Query(value = "SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            " count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,sum(od.price) as price,od.condition,cf.ville_id," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id   FROM order_details od " +
            " LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where od.condition='Due' and sh.ship_order=?1", nativeQuery = true)
    OrderDetails countFactureDue(String id);
    @Query(value = "SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            " count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,sum(od.price) as price,od.condition,cf.ville_id," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id   FROM order_details od " +
            " LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where od.condition='Payé' and sh.ship_order=?1", nativeQuery = true)
    OrderDetails countFacturePayer(String id);
    @Query(value = "SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            " count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,sum(od.price) as price,od.condition,cf.ville_id," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id   FROM order_details od " +
            " LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where (od.condition IS NULL or od.condition='N/A' or od.condition='') " +
            "and sh.ship_order=?1", nativeQuery = true)
    OrderDetails countFactureNA(String id);
    @Query(value = "SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            " count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,sum(od.price) as price,od.condition,cf.ville_id," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id   FROM order_details od " +
            " LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where sh.ship_order=?1", nativeQuery = true)
    OrderDetails countFacture(String id);

    @Query(value = " SELECT od.id,od.delivery_date,od.pounds,od.status,od.sub_total," +
            "count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 group by abreger", nativeQuery = true)
    List<OrderDetails> countOrderDetailsColisParVille(Long code);
    @Query(value = " SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "od.upc_colis as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 group by abreger", nativeQuery = true)
    List<OrderDetails> countOrderDetailsAmountParVille(Long code);

    @Query(value = "SELECT od.id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            " count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,sum(od.price) as price,od.condition,cf.ville_id," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id   FROM order_details od " +
            " LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id where od.condition='Due' and cf.ville_id=?1", nativeQuery = true)
    OrderDetails sumAmountFactureFromMyCity(Long cityID);

    @Query(value = " SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "od.upc_colis as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 and od.condition='Payé' group by abreger", nativeQuery = true)
    List<OrderDetails> countFacturePayerParVille(Long code);
    @Query(value = " SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "od.upc_colis as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 and od.condition='Due' group by abreger", nativeQuery = true)
    List<OrderDetails> countFactureDueParVille(Long code);
    @Query(value = " SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "od.upc_colis as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 and od.condition IS NULL or od.condition='N/A' or od.condition='' group by abreger", nativeQuery = true)
    List<OrderDetails> countFactureNAParVille(Long code);

    @Query(value = " SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "od.upc_colis as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 and od.status='Commande a été livrée.'", nativeQuery = true)
    OrderDetails countColisLivre(Long code);
    @Query(value = " SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "od.upc_colis as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 and od.status!='Commande a été livrée.'", nativeQuery = true)
    OrderDetails countColisStocker(Long code);
    @Query(value = " SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "od.upc_colis as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 and od.status='Commande a été livrée.' group by abreger", nativeQuery = true)
    List<OrderDetails> countColisLivreParVille(Long code);
    @Query(value = " SELECT count(od.id) as id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "od.upc_colis as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 and od.status!='Commande a été livrée.' group by abreger", nativeQuery = true)
    List<OrderDetails> countColisStockerParVille(Long code);
    @Query(value = " SELECT od.id,od.delivery_date,sum(od.pounds) as pounds,od.status,sum(od.sub_total) as sub_total," +
            "count(od.upc_colis) as upc_colis,od.colis_type,od.category_id,od.exp_email,od.exp_name,od.exp_phone,od.created_at," +
            " od.rec_email,od.rec_name,od.rec_phone,od.tracking,od.douane,od.price,od.condition,cf.ville_id, v.abreger," +
            " od.city_fees,od.client_id,od.order_id,sh.ship_order,od.colis_image,od.note,od.user_id    FROM order_details od LEFT JOIN ship_order sh ON sh.id=od.order_id " +
            " left join city_vs_fees cf on od.city_fees=cf.id left join ville v on v.id=cf.ville_id " +
            "where od.order_id= ?1 and od.status='Commande prête à être livrée.' and od.rec_name=?2 and " +
            "od.rec_phone=?3 and cf.ville_id=?4", nativeQuery = true)
    OrderDetails findDataMessage(Long orderID, String clientName, String clientPhone, Long cityID);




    @Query(value = " SELECT * FROM order_details od " +
            " where od.upc_colis LIKE CONCAT('%', :param, '%')" +
            "    OR od.rec_name LIKE CONCAT('%', :param, '%')" +
            "    OR od.tracking LIKE CONCAT('%', :param, '%')" +
            "    OR od.rec_phone LIKE CONCAT('%', :param, '%')" +
            "    OR od.rec_email LIKE CONCAT('%', :param, '%')" +
            "    OR od.exp_name LIKE CONCAT('%', :param, '%')" +
            "    OR od.exp_phone LIKE CONCAT('%', :param, '%')" +
            "    OR od.exp_email LIKE CONCAT('%', :param, '%')" +
            " ORDER BY od.id DESC" +
            "", nativeQuery = true)
    Page<OrderDetails> findAllByParam(@Param("param") String param, Pageable pageable);

    @Query(value = " SELECT * FROM order_details od " +
            " where od.upc_colis LIKE CONCAT('%', :param, '%')" +
            "    OR od.rec_name LIKE CONCAT('%', :param, '%')" +
            "    OR od.rec_phone LIKE CONCAT('%', :param, '%')" +
            "    OR od.rec_email LIKE CONCAT('%', :param, '%')" +
            "    OR od.exp_name LIKE CONCAT('%', :param, '%')" +
            "    OR od.exp_phone LIKE CONCAT('%', :param, '%')" +
            "    OR od.exp_email LIKE CONCAT('%', :param, '%')" +
            "", nativeQuery = true)
    List<OrderDetails> findAllByParamForDownload(@Param("param") String param);
}
