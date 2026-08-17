package com.velogexpress.service.impl;

import com.velogexpress.entity.Taux;
import com.velogexpress.mapper.TauxMapper;
import com.velogexpress.model.TauxModel;
import com.velogexpress.repository.TauxRepository;
import com.velogexpress.service.TauxService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TauxServiceImpl implements TauxService {

    private final TauxRepository tauxRepository;

    @Override
    public TauxModel createTaux(TauxModel tauxModel) {
        Taux taux = TauxMapper.mapToTaux(tauxModel);
        switch(tauxModel.getDevise()){
            case "Dollars US"->taux.setSymbole("$US");
            case "Dollars CAN"->taux.setSymbole("$CAN");
            case "Pesos RD"->taux.setSymbole("$RD");
            case "Euros"->taux.setSymbole("€");
        }
        return TauxMapper.mapToTauxModel(tauxRepository.save(taux));
    }

    @Override
    public Page<TauxModel> getAllTaux(Pageable pageable) {
        return tauxRepository.findAll(pageable)
                .map(TauxMapper::mapToTauxModel);
    }

    @Override
    public Page<TauxModel> getTauxByAllDevise(String description, Pageable pageable) {
        return tauxRepository.findByAllDevise(description, pageable)
                .map(TauxMapper::mapToTauxModel);
    }

    @Override
    public TauxModel getTauxByDevise(String description) {
        Taux taux = tauxRepository.findByDevise(description);
        return TauxMapper.mapToTauxModel(taux);
    }

    @Override
    public Page<TauxModel> getTauxByDevise(String description,Pageable pageable) {
       return tauxRepository.findByDevise(description, pageable).map(TauxMapper::mapToTauxModel);

    }

    @Override
    public TauxModel updateTaux(Long id, TauxModel tauxModel) {
        Taux existing = tauxRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Aucun taux ne correspond a l'indentifiant: "+id));
        if (existing == null) return null;
        existing.setBuy(tauxModel.getBuy());
        existing.setSale(tauxModel.getSale());

        return TauxMapper.mapToTauxModel(tauxRepository.save(existing));
    }

    @Override
    public void deleteTaux(Long id) {
        Taux existing = tauxRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Aucun taux ne correspond a l'indentifiant: "+id));
        if (existing != null) {
            tauxRepository.delete(existing);
        }
    }
}
