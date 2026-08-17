package com.velogexpress.mapper;

import com.velogexpress.entity.Mainaddress;
import com.velogexpress.model.MainaddressModel;

public class MainaddressMapper {
    public static MainaddressModel mapToMainaddressModel(Mainaddress mainaddress){
        return new MainaddressModel(
                mainaddress.getId(),
                mainaddress.getAddressline(),
                mainaddress.getCity(),
                mainaddress.getState(),
                mainaddress.getZipcode(),
                mainaddress.getPhone()
        );
    }

    public static Mainaddress mapToMainaddress(MainaddressModel mainaddressModel){
        return new Mainaddress(
                mainaddressModel.getId(),
                mainaddressModel.getAddressline(),
                mainaddressModel.getCity(),
                mainaddressModel.getState(),
                mainaddressModel.getZipcode(),
                mainaddressModel.getPhone()
        );
    }
}
