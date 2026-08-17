package com.velogexpress.service;

import com.velogexpress.model.FactureModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface FactureService {
    FactureModel createFacture(FactureModel factureModel);
    FactureModel createQuickFacture(FactureModel factureModel);
    Page<FactureModel> getAllFacture(Pageable pageable);
    Optional<FactureModel> getFactureByCode(String code);
    void deleteFacture(String code);
    void printFacture();
    Page<FactureModel> searchFacture(String code, Pageable pageable);
    Page<FactureModel> getFactureDetailsWith(String client,Long order, Pageable pageable);
    Optional<FactureModel> updateFacture(String code);
    FactureModel getFactureToday();
    FactureModel getFactureToday(Long ID);
    Page<FactureModel> getCountAllFacture(Long order,Pageable pageable);
    Page<FactureModel> getCountAllFactureBySurcursal(Long order,Long surcursal,Pageable pageable);

}
