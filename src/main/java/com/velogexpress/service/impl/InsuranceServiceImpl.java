package com.velogexpress.service.impl;

import com.velogexpress.entity.Insurance;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.InsuranceMapper;
import com.velogexpress.model.InsuranceModel;
import com.velogexpress.repository.InsuranceRepository;
import com.velogexpress.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;

    @Override
    @Transactional
    public InsuranceModel createInsurance(InsuranceModel inssuranceModel) {
        Insurance inssurance = InsuranceMapper.mapToInssurance(inssuranceModel);
        Insurance saved = insuranceRepository.save(inssurance);
        return InsuranceMapper.mapToInssuranceModel(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceModel> getAllInsurance(Pageable pageable) {
        return insuranceRepository.findAll(pageable)
                .map(InsuranceMapper::mapToInssuranceModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceModel> getInsuranceByAmount(Double amount, Pageable pageable) {
        return insuranceRepository.findByAmount(amount, pageable)
                .map(InsuranceMapper::mapToInssuranceModel);
    }

    @Override
    @Transactional(readOnly = true)
    public InsuranceModel getInsuranceById(Long id) {
        Insurance inssurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Inssurance not found for id: " + id));
        return InsuranceMapper.mapToInssuranceModel(inssurance);
    }

    @Override
    @Transactional
    public InsuranceModel updateInsurance(Long id, InsuranceModel inssuranceModel) {
        Insurance existing = insuranceRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Inssurance not found for id: " + id));

        // Update only allowed fields
        existing.setAmount(inssuranceModel.getAmount());

        Insurance updated = insuranceRepository.save(existing);
        return InsuranceMapper.mapToInssuranceModel(updated);
    }

    @Override
    @Transactional
    public void deleteInsurance(Long id) {
        Insurance inssurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Inssurance not found for id: " + id));
        insuranceRepository.delete(inssurance);
    }
}
