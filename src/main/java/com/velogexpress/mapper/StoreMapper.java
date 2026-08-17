package com.velogexpress.mapper;

import com.velogexpress.entity.Store;
import com.velogexpress.model.StoreModel;

public class StoreMapper {
    public static Store mapToStore(StoreModel storeModel){
        return new Store(
                storeModel.getId(),
                storeModel.getOrderdetails(),
                storeModel.getTag(),
                storeModel.getStatus()
        );
    }

    public static StoreModel mapToStoreModel(Store store){
        return new StoreModel(
                store.getId(),
                store.getOrderdetails(),
                store.getTag(),
                store.getStatus()
        );
    }
}
