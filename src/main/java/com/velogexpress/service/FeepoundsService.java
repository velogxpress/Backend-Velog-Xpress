package com.velogexpress.service;

import com.velogexpress.model.FeepoundsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FeepoundsService {
    FeepoundsModel createFee(FeepoundsModel feepoundsModel);
    Page<FeepoundsModel> getAllFee(Pageable pageable);
    Page<FeepoundsModel> getFeeByAmount(Double Id, Pageable pageable);
    FeepoundsModel getFeeById(Long Id);
    FeepoundsModel updateFee(Long Id, FeepoundsModel feepoundsModel);
    void deleteFee(Long Id);
}
