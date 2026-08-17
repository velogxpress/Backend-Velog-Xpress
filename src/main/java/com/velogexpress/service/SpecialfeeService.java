package com.velogexpress.service;

import com.velogexpress.model.SpecialfeeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SpecialfeeService {
    SpecialfeeModel createSpecialfee(SpecialfeeModel specialfeeModel);
    Page<SpecialfeeModel> getAllSpecialfee(Pageable pageable);
    Page<SpecialfeeModel> getSpecialfeeByAmount(Double id, Pageable pageable);
    SpecialfeeModel getSpecialfeeById(Long id);
    SpecialfeeModel updateSpecialfee(Long id,SpecialfeeModel specialfeeModel);
    void deleteSpecialfee(Long id);
}
