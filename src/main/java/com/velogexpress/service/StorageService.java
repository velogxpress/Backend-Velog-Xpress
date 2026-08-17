package com.velogexpress.service;

import com.velogexpress.entity.Storage;
import com.velogexpress.model.StorageModel;

import java.util.List;

public interface StorageService {
    StorageModel createStorage(StorageModel storageModel);
    List<StorageModel> getAllStorage();
    List<StorageModel> getAllStorage(String param);
    StorageModel getStorageByContainer(String container);
    StorageModel updateStorage(Long id,StorageModel storageModel);

}
