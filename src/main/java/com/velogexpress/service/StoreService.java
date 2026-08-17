package com.velogexpress.service;

import com.velogexpress.model.StoreModel;

import java.util.List;

public interface StoreService {
    StoreModel createStore(StoreModel storeModel);
    List<StoreModel> getStores();
    StoreModel getStore(Long orderdetailsID);
    StoreModel updateStore(Long id,StoreModel storeModel);
}
