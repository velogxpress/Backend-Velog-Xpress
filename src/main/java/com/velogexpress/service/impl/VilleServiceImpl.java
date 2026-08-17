package com.velogexpress.service.impl;

import com.velogexpress.entity.Region;
import com.velogexpress.entity.Ville;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.VilleMapper;
import com.velogexpress.model.VilleModel;
import com.velogexpress.repository.RegionRepository;
import com.velogexpress.repository.VilleRepository;
import com.velogexpress.service.VilleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class  VilleServiceImpl implements VilleService {

    private final VilleRepository villeRepository;
    private final RegionRepository regionRepository;

    @Override
    @Transactional
    public VilleModel createVille(VilleModel villeModel) {
        // Find Region
        Region region = regionRepository.findByDescription(
                villeModel.getRegion().getDescription()
        );

        if (region == null) {
            throw new RessourceNotFoundException(
                    "Region does not exist with description: " + villeModel.getRegion().getDescription()
            );
        }

        // Map VilleModel -> Ville and assign region
        Ville ville = VilleMapper.mapToVille(villeModel);
        ville.setRegion(region);

        // Save and return
        Ville savedVille = villeRepository.save(ville);
        return VilleMapper.mapToVilleModel(savedVille);
    }

    @Override
    public Page<VilleModel> getAllVille(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("description").ascending());
        return villeRepository.findAll(pageable).map(VilleMapper::mapToVilleModel);
    }

    @Override
    public VilleModel getVilleByID(Long villeId) {
        Ville ville = villeRepository.findById(villeId)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Ville does not exist with ID: " + villeId
                ));
        return VilleMapper.mapToVilleModel(ville);
    }

    @Override
    public VilleModel getVilleByDescription(String description) {
        Ville ville = villeRepository.findByDescription(description)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Ville does not exist with description: " + description
                ));
        return VilleMapper.mapToVilleModel(ville);
    }


    @Override
    public Page<VilleModel> getVilleByRegion(Long description, Pageable pageable) {
        Page<Ville> villes = villeRepository.findByRegion(description, pageable);

        return villes.map(VilleMapper::mapToVilleModel);
    }


    @Override
    @Transactional
    public VilleModel updateVille(Long villeId, VilleModel villeModel) {
        Ville existingVille = villeRepository.findById(villeId)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Ville does not exist with ID: " + villeId
                ));

        Region region = regionRepository.findByDescription(
                villeModel.getRegion().getDescription()
        );

        if (region == null) {
            throw new RessourceNotFoundException(
                    "Region does not exist with description: " + villeModel.getRegion().getDescription()
            );
        }

        existingVille.setDescription(villeModel.getDescription());
        existingVille.setAbreger(villeModel.getAbreger());
        existingVille.setRegion(region);

        Ville updatedVille = villeRepository.save(existingVille);
        return VilleMapper.mapToVilleModel(updatedVille);
    }

    @Override
    @Transactional
    public void deleteVille(Long villeId) {
        Ville ville = villeRepository.findById(villeId)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Ville does not exist with ID: " + villeId
                ));
        villeRepository.delete(ville);
    }
}
