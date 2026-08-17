package com.velogexpress.service.impl;

import com.velogexpress.entity.Cipinfee;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.CipinfeeMapper;
import com.velogexpress.model.CipinfeeModel;
import com.velogexpress.repository.CipinfeeRepository;
import com.velogexpress.service.CipinfeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CipinfeeServiceImpl implements CipinfeeService {
    private CipinfeeRepository cipinfeeRepository;

    @Override
    public CipinfeeModel createCipinfee(CipinfeeModel cipinfeeModel) {
        Cipinfee cipinfee= CipinfeeMapper.mapToCipinfee(cipinfeeModel);
        Cipinfee saveobj=cipinfeeRepository.save(cipinfee);
        return CipinfeeMapper.mapToCipinfeeModel(saveobj);
    }

    @Override
    public Page<CipinfeeModel> getAllCipinfee(Pageable pageable) {
        return cipinfeeRepository.findAll(pageable)
                .map(CipinfeeMapper::mapToCipinfeeModel);
    }

    @Override
    public CipinfeeModel getCipinfeeById(Long id) {
        Cipinfee cipinfee=cipinfeeRepository.findById(id)
                .orElseThrow(
                        ()->new RessourceNotFoundException("City vs Fees not exists with given id "+id)
                );
        return CipinfeeMapper.mapToCipinfeeModel(cipinfee);
    }

    @Override
    public Page<CipinfeeModel> getCipinfeeByCity(String cityID, Pageable pageable) {
        return cipinfeeRepository.findByCityDesc(cityID, pageable)
                .map(CipinfeeMapper::mapToCipinfeeModel);
    }

    @Override
    public CipinfeeModel getCipinfeeByCity(Long cityID) {
        Cipinfee cipinfee=cipinfeeRepository.findByCityID(cityID);
        if(cipinfee!=null){
            return CipinfeeMapper.mapToCipinfeeModel(cipinfee);
        }else{
            return null;
        }

    }

    @Override
    public CipinfeeModel updateCipinfeeById(Long id, CipinfeeModel cipinfeeModel) {
        Cipinfee existing = cipinfeeRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("City vs Fees not exists with given id " + id));

        // Convert model → entity
        Cipinfee updated = CipinfeeMapper.mapToCipinfee(cipinfeeModel);

        // Preserve the existing ID so JPA updates instead of inserting
        updated.setId(existing.getId());

        Cipinfee saved = cipinfeeRepository.save(updated);

        return CipinfeeMapper.mapToCipinfeeModel(saved);
    }


    @Override
    public void deleteCipinfee(Long id) {
        Cipinfee cipinfee=cipinfeeRepository.findById(id)
                .orElseThrow(()->new RessourceNotFoundException("City vs Fees not exists with given id "+id));
        cipinfeeRepository.delete(cipinfee);
    }
}
