package com.velogexpress.mapper;

import com.velogexpress.entity.Storage;
import com.velogexpress.model.StorageModel;

public class StorageMapper {
    public static StorageModel mapToStorageModel(Storage storage) {
        return new StorageModel(
                storage.getId(),
                storage.getOrder(),
                storage.getContainer(),
                storage.getDescription(),
                storage.getAirwaybill()
        );
    }

    public static Storage mapToStorage(StorageModel storageModel) {
        return new Storage(
                storageModel.getId(),
                storageModel.getOrder(),
                storageModel.getContainer(),
                storageModel.getDescription(),
                storageModel.getAirwaybill()
        );
    }
}
