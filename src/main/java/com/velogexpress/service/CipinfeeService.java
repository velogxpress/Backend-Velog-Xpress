package com.velogexpress.service;

import com.velogexpress.model.CipinfeeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CipinfeeService {
    CipinfeeModel createCipinfee(CipinfeeModel cipinfeeModel);
    Page<CipinfeeModel> getAllCipinfee(Pageable pageable);
    CipinfeeModel getCipinfeeById(Long id);
    Page<CipinfeeModel> getCipinfeeByCity(String cityID, Pageable pageable);
    CipinfeeModel getCipinfeeByCity(Long cityID);
    CipinfeeModel updateCipinfeeById(Long id,CipinfeeModel cipinfeeModel);
    void deleteCipinfee(Long id);
}
