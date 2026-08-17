package com.velogexpress.service;

import com.velogexpress.model.FactureDetailsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FactureDetailsService {
    FactureDetailsModel createFactureDetails(FactureDetailsModel factureDetailsModel);
    FactureDetailsModel createQuickFactureDetails(FactureDetailsModel factureDetailsModel);
    Page<FactureDetailsModel> getAllFactureDetails(Pageable pageable);
    Page<FactureDetailsModel> getFactureDetails(String factureID, Pageable pageable);
    Optional<FactureDetailsModel> getSingleFactureDetails(String factureID);
    void deleteFactureDetails(Long colis);
}
