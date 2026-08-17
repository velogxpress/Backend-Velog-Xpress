package com.velogexpress.service;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.model.ClientregisterModel;

public interface LoginService {
    Clientregister getAutheticated(String email,String pwrd);
}
