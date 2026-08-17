package com.velogexpress.service;

import com.velogexpress.model.VilleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VilleService {
    VilleModel createVille(VilleModel villeModel);
//    Page<VilleModel> getAllVille();

    Page<VilleModel> getAllVille(int page, int size);

    VilleModel getVilleByID(Long villeId);
    VilleModel getVilleByDescription(String description);
    Page<VilleModel> getVilleByRegion(Long description, Pageable pageable);
    VilleModel updateVille(Long villeId, VilleModel villeModel);
    void deleteVille(Long villeId);
}
