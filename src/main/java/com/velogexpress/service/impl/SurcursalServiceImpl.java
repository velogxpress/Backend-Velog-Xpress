package com.velogexpress.service.impl;

import com.velogexpress.entity.Surcursal;
import com.velogexpress.entity.Ville;
import com.velogexpress.mapper.SurcursalMapper;
import com.velogexpress.model.SurcursalModel;
import com.velogexpress.repository.SurcursalRepository;
import com.velogexpress.repository.VilleRepository;
import com.velogexpress.service.SurcursalService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SurcursalServiceImpl implements SurcursalService {

    private final SurcursalRepository surcursalRepository;
    private final VilleRepository villeRepository;

    @Override
    public SurcursalModel createSurcursal(SurcursalModel surcursalModel) {
        Surcursal surcursal = SurcursalMapper.mapToSurcursal(surcursalModel);
        Surcursal savedSurcursal = surcursalRepository.save(surcursal);
        return SurcursalMapper.mapToSurcursalModel(savedSurcursal);
    }

    @Override
    public Page<SurcursalModel> getAllSurcursal(Pageable pageable) {
        return surcursalRepository.findAll(pageable)
                .map(SurcursalMapper::mapToSurcursalModel);
    }

    @Override
    public Page<SurcursalModel> getSurcursal(String ville, Pageable pageable) {
        return surcursalRepository.search(ville, pageable)
                .map(SurcursalMapper::mapToSurcursalModel);
    }

    @Override
    public SurcursalModel getSurcursalByName(String surcursalName) {
        return Optional.ofNullable(surcursalRepository.findByName(surcursalName))
                .map(SurcursalMapper::mapToSurcursalModel)
                .orElse(null);
    }

    @Override
    public Page<SurcursalModel> getSurcursalByVille(String ville, Pageable pageable) {
        return surcursalRepository.findByVille(ville, pageable)
                .map(SurcursalMapper::mapToSurcursalModel);
    }

    @Override
    public SurcursalModel updateSurcursal(String surcursalName, SurcursalModel surcursalModel) {
        Surcursal surcursal = surcursalRepository.findByName(surcursalName);
        if (surcursal == null) {
            return null;
        }

        Ville ville = villeRepository.findById(surcursalModel.getVille().getId())
                .orElseThrow(() -> new RuntimeException("Ville not found"));
        surcursal.setName(surcursalModel.getName());
        surcursal.setAddress(surcursalModel.getAddress());
        surcursal.setPhone(surcursalModel.getPhone());
        surcursal.setVille(ville);
        surcursal.setHoraire(surcursalModel.getHoraire());

        Surcursal updatedSurcursal = surcursalRepository.save(surcursal);
        return SurcursalMapper.mapToSurcursalModel(updatedSurcursal);
    }

    @Override
    public void deleteSurcursal(String surcursalName) {
        Optional.ofNullable(surcursalRepository.findByName(surcursalName))
                .ifPresent(surcursalRepository::delete);
    }
}
