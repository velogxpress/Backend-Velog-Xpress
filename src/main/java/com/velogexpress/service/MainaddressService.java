package com.velogexpress.service;

import com.velogexpress.model.MainaddressModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MainaddressService {
    MainaddressModel createAddress(MainaddressModel mainaddressModel);
    Page<MainaddressModel> getAddress(Pageable pageable);
    MainaddressModel getAddressById(Long id);
    MainaddressModel updateAddress(Long addressId,MainaddressModel mainaddressModel, Pageable pageable);
}
