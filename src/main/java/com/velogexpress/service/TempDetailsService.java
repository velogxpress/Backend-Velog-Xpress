package com.velogexpress.service;

import com.velogexpress.model.TempDetailsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TempDetailsService {
    TempDetailsModel createFactureDetailsTMP(TempDetailsModel tempDetailsModel);
    Page<TempDetailsModel> getAllFactureDetailsTMP(String client, Pageable pageable);
    TempDetailsModel getSingleFactureDetailsTMP(String factureID);
    TempDetailsModel getSingleDetailsTMP(Long id);
    TempDetailsModel updateFactureDetailsTMP(Long id,TempDetailsModel tempDetailsModel);
    void deleteFactureDetailsTMP(Long colis);
    void deleteAllFactureDetailsTMP(String client, Pageable pageable);
}
