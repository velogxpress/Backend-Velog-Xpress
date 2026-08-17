package com.velogexpress.service.impl;

import com.velogexpress.entity.Store;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.StoreMapper;
import com.velogexpress.model.StoreModel;
import com.velogexpress.repository.StoreRepository;
import com.velogexpress.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {
    private StoreRepository storeRepository;

    @Override
    public StoreModel createStore(StoreModel storeModel) {
        Store store= StoreMapper.mapToStore(storeModel);
        Store saveObj = storeRepository.save(store);
        return StoreMapper.mapToStoreModel(saveObj);
    }

    @Override
    public List<StoreModel> getStores() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream().map(
                StoreMapper::mapToStoreModel
        ).collect(Collectors.toList());
    }

    @Override
    public StoreModel getStore(Long orderdetailsID) {
        Store  store = storeRepository.findByOrderDetails(orderdetailsID);
        if(store == null){
            return null;
        }else{
            return StoreMapper.mapToStoreModel(store);
        }
    }

    @Override
    public StoreModel updateStore(Long id, StoreModel storeModel) {
        Store store = storeRepository.findByOrderDetails(id);
        if(store == null){
            return null;
        }else{
            store.setStatus(storeModel.getStatus());
            Store saveObj = storeRepository.save(store);
            return StoreMapper.mapToStoreModel(saveObj);
        }
    }
}
