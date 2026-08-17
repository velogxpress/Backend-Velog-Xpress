package com.velogexpress.mapper;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.model.RegisterModel;

public class RegisterMapper {
    public static RegisterModel mapToRegisterModel(Clientregister clientregister) {
        return new RegisterModel(
                clientregister.getId(),
                clientregister.getName(),
                clientregister.getEmail(),
                clientregister.getAddress(),
                clientregister.getVille(),
                clientregister.getUsercode(),
                clientregister.getPhone(),
                clientregister.getRole(),
                clientregister.getStatus()
        );
    }
}
