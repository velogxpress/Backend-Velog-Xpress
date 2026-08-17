package com.velogexpress.service;

import com.velogexpress.model.InsuranceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InsuranceService {
    InsuranceModel createInsurance(InsuranceModel inssuranceModel);
    Page<InsuranceModel> getAllInsurance(Pageable pageable);
    Page<InsuranceModel> getInsuranceByAmount(Double id, Pageable pageable);
    InsuranceModel getInsuranceById(Long id);
    InsuranceModel updateInsurance(Long id, InsuranceModel inssuranceModel);
    void deleteInsurance(Long id);
}
