package com.velogexpress.service.impl;

import com.velogexpress.entity.StorageDetails;
import com.velogexpress.mapper.StorageDetailsMapper;
import com.velogexpress.model.StorageDetailsModel;
import com.velogexpress.repository.StorageDetailsRepository;
import com.velogexpress.service.StorageDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StorageDetailsImplService implements StorageDetailsService {
    private StorageDetailsRepository storageDetailsRepository;

    @Override
    public StorageDetailsModel create(StorageDetailsModel storageDetailsModel) {
        StorageDetails storageDetails = StorageDetailsMapper.mapToStorageDetails(storageDetailsModel);
        StorageDetails saveObj = storageDetailsRepository.save(storageDetails);
        return StorageDetailsMapper.mapToStorageDetailsModel(saveObj);
    }

    @Override
    public List<StorageDetailsModel> getStorageDetails(String order) {
        List<StorageDetails> storageDetails = storageDetailsRepository.findAllByDesc(order);
        return storageDetails.stream().map(
                StorageDetailsMapper::mapToStorageDetailsModel
        ).collect(Collectors.toList());
    }

    @Override
    public List<StorageDetailsModel> getStorageDetails(String order,String container) {
        List<StorageDetails> storageDetails = storageDetailsRepository.searchStorage(order,container);
        if (storageDetails.isEmpty()) {
            return Collections.emptyList();
        }else{
            return storageDetails.stream().map(
                    StorageDetailsMapper::mapToStorageDetailsModel
            ).collect(Collectors.toList());
        }

    }

    @Override
    public List<StorageDetailsModel> getCountStorageDetails(String order) {
        List<StorageDetails> storageDetails = storageDetailsRepository.findByContainer(order);
        if (storageDetails.isEmpty()) {
            return Collections.emptyList();
        }else{
            return storageDetails.stream().map(
                    StorageDetailsMapper::mapToStorageDetailsModel
            ).collect(Collectors.toList());
        }
    }

}
