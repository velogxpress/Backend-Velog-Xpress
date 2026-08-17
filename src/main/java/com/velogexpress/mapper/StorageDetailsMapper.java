package com.velogexpress.mapper;

import com.velogexpress.entity.StorageDetails;
import com.velogexpress.model.StorageDetailsModel;

public class StorageDetailsMapper {
    public static StorageDetailsModel mapToStorageDetailsModel(StorageDetails storageDetails) {
        return new StorageDetailsModel(
                storageDetails.getId(),
                storageDetails.getStorage(),
                storageDetails.getOrderdetails()
        );
    }

    public static StorageDetails mapToStorageDetails(StorageDetailsModel storageDetailsModel) {
        return new StorageDetails(
                storageDetailsModel.getId(),
                storageDetailsModel.getStorage(),
                storageDetailsModel.getOrderdetails()
        );
    }
}
