package com.velogexpress.repository;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.model.ClientregisterModel;
import com.velogexpress.projection.ClientGraphProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRegisterRepository extends JpaRepository<Clientregister, Long> {

    @Query(value = "SELECT * FROM client_register WHERE email = :code OR usercode = :code", nativeQuery = true)
    Clientregister findByUserCodeOrEmail(@Param("code") String code);

    @Query(value = "SELECT * FROM client_register WHERE email = :email AND password = :pwrd AND status = 'Actif(ve)'", nativeQuery = true)
    Clientregister authenticateUser(@Param("email") String email, @Param("pwrd") String pwrd);

    @Query(value = "SELECT * FROM client_register WHERE email = :email AND status = 'Actif(ve)'", nativeQuery = true)
    Clientregister authenticateUser(@Param("email") String email);

    @Query(value = "SELECT * FROM client_register ORDER BY id DESC", nativeQuery = true)
    Page<Clientregister> findAllOrdered(Pageable pageable);

    @Query(value = """
        SELECT * FROM client_register
        WHERE LOWER(usercode) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(phone) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(email) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(full_name) LIKE LOWER(CONCAT('%', :param, '%'))
        ORDER BY id DESC
        """, nativeQuery = true)
    Page<Clientregister> search(@Param("param") String param, Pageable pageable);

    @Query(value = "SELECT * FROM client_register WHERE role = :role or role='Admin'", nativeQuery = true)
    Page<Clientregister> findByRole(@Param("role") String role, Pageable pageable);

    // Use a projection for grouped/aggregated data
    @Query(value = """
        SELECT COUNT(cl.id) AS total,
               v.description AS villeDescription
        FROM client_register cl
        LEFT JOIN ville v ON v.id = cl.ville_id
        GROUP BY v.description
        """, nativeQuery = true)
    Page<ClientGraphProjection> countByCity(Pageable pageable);
    @Query(value = """
        SELECT COUNT(cl.id) AS id,cl.address,cl.usercode,cl.ville_id,cl.full_name,cl.phone,cl.email,cl.password,cl.status,
               cl.role,v.description AS description,v.abreger
        FROM client_register cl
        LEFT JOIN ville v ON v.id = cl.ville_id
        GROUP BY v.abreger
        """, nativeQuery = true)
    List<Clientregister> findClientandCity();
}
