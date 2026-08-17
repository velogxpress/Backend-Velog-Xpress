package com.velogexpress.service;

import com.velogexpress.model.ClientregisterModel;
import com.velogexpress.model.RegisterModel;
import com.velogexpress.projection.ClientGraphProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ClientregisterService {
    ClientregisterModel createUser(ClientregisterModel clientregisterModel);
    ClientregisterModel createUtilisateur(ClientregisterModel clientregisterModel);
    Page<ClientregisterModel> getAllClienteregister(Pageable pageable);
    Page<ClientregisterModel> getAllAgent(Pageable pageable);
    Page<ClientregisterModel> getAgent(String param, Pageable pageable);
    ClientregisterModel getClientregisterByUsercode(String code);
    ClientregisterModel updateClientregister(String code,ClientregisterModel clientregisterModel);
    ClientregisterModel updatePassword(String code,ClientregisterModel clientregisterModel);
    Long getCountUser(String user, Pageable pageable);
    List<ClientregisterModel> getCountGraphe();
    Long countClient();
    ClientregisterModel EditUserInfo(String id,ClientregisterModel clientregisterModel);
    void deleteUser(String id);
    String findExistEmail(String email);
    RegisterModel getRegisterByUsercode(String code);

}
