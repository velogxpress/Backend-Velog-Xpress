package com.velogexpress.service;

import com.velogexpress.model.StorageDetailsModel;

import java.util.List;

public interface StorageDetailsService {
    StorageDetailsModel create(StorageDetailsModel storageDetailsModel);
    List<StorageDetailsModel> getStorageDetails(String order);
    List<StorageDetailsModel> getStorageDetails(String order,String container);
    List<StorageDetailsModel> getCountStorageDetails(String order);

}
