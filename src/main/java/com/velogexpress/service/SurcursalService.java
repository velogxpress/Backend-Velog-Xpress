package com.velogexpress.service;

import com.velogexpress.model.SurcursalModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SurcursalService {
    SurcursalModel createSurcursal(SurcursalModel surcursalModel);
    Page<SurcursalModel> getAllSurcursal(Pageable pageable);
    Page<SurcursalModel> getSurcursal(String ville, Pageable pageable);
    SurcursalModel getSurcursalByName(String surcursalID);
    Page<SurcursalModel> getSurcursalByVille(String ville, Pageable pageable);
    SurcursalModel updateSurcursal(String surcursalName,SurcursalModel surcursalModel);
    void deleteSurcursal(String surcursalID);

}
