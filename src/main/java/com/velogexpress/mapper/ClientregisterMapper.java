package com.velogexpress.mapper;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.model.ClientregisterModel;

public class ClientregisterMapper {
    public static ClientregisterModel mapToclientregisterModel(Clientregister clientregister){
        return  new ClientregisterModel(
                clientregister.getId(),
                clientregister.getName(),
                clientregister.getEmail(),
                clientregister.getAddress(),
                clientregister.getVille(),
                clientregister.getUsercode(),
                clientregister.getPassword(),
                clientregister.getPhone(),
                clientregister.getRole(),
                clientregister.getStatus()
        );
    }

    public static  Clientregister mapToclientregister(ClientregisterModel clientregisterModel){
        return new Clientregister(
                clientregisterModel.getId(),
                clientregisterModel.getName(),
                clientregisterModel.getEmail(),
                clientregisterModel.getAddress(),
                clientregisterModel.getVille(),
                clientregisterModel.getUsercode(),
                clientregisterModel.getPassword(),
                clientregisterModel.getPhone(),
                clientregisterModel.getRole(),
                clientregisterModel.getStatus()
        );
    }
}
