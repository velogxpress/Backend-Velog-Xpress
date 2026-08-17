package com.velogexpress.service;

import com.velogexpress.model.TauxModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TauxService {
    TauxModel createTaux(TauxModel TauxModel);
    Page<TauxModel> getAllTaux(Pageable pageable);
    Page<TauxModel> getTauxByAllDevise(String description, Pageable pageable);
    TauxModel getTauxByDevise(String description);
    Page<TauxModel> getTauxByDevise(String description,Pageable pageable);
    TauxModel updateTaux(Long id,TauxModel TauxModel);
    void deleteTaux(Long id);
}
