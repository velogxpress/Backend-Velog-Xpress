package com.velogexpress.service.impl;

import com.velogexpress.entity.Storage;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.StorageMapper;
import com.velogexpress.model.StorageModel;
import com.velogexpress.repository.StorageRepository;
import com.velogexpress.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StorageImplService implements StorageService {
    private StorageRepository storageRepository;
    @Override
    public StorageModel createStorage(StorageModel storageModel) {
        Storage storage= StorageMapper.mapToStorage(storageModel);
        Storage saveObj=storageRepository.save(storage);
        return StorageMapper.mapToStorageModel(saveObj);
    }

    @Override
    public List<StorageModel> getAllStorage() {
        List<Storage> storageList = storageRepository.findAllByDesc();
        return storageList.stream().map(
                StorageMapper::mapToStorageModel
        ).collect(Collectors.toList());
    }

    @Override
    public List<StorageModel> getAllStorage(String param) {
        List<Storage> storageList = storageRepository.findAllByDesc(param);
        if(storageList.isEmpty()){
            return null;
        }else{
            return storageList.stream().map(
                    StorageMapper::mapToStorageModel
            ).collect(Collectors.toList());
        }

    }

    @Override
    public StorageModel getStorageByContainer(String container) {
        Storage storage = storageRepository.findByContainer(container);
        if(storage == null) {
            return null;
        }else{
            return StorageMapper.mapToStorageModel(storage);
        }
    }

    @Override
    public StorageModel updateStorage(Long id, StorageModel storageModel) {
        Storage storage = storageRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Storage not exists with given id " + id));
        if(storage == null) {
            return null;
        }else{
            storage.setDescription(storageModel.getDescription());
            storage.setAirwaybill(storageModel.getAirwaybill());
            Storage  saveObj=storageRepository.save(storage);
            return StorageMapper.mapToStorageModel(saveObj);
        }
    }
}
