package com.velogexpress.service.impl;

import com.velogexpress.entity.Feepounds;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.FeepoundsMapper;
import com.velogexpress.model.FeepoundsModel;
import com.velogexpress.repository.FeepoundsRepository;
import com.velogexpress.service.FeepoundsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeepoundsImpl implements FeepoundsService {

    private final FeepoundsRepository feepoundsRepository;

    @Override
    public FeepoundsModel createFee(FeepoundsModel feepoundsModel) {
        Feepounds entity = FeepoundsMapper.mapToFeepounds(feepoundsModel);
        Feepounds saved = feepoundsRepository.save(entity);
        return FeepoundsMapper.mapToFeepoundsModel(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeepoundsModel> getAllFee(Pageable pageable) {
        return feepoundsRepository.findAll(pageable)
                .map(FeepoundsMapper::mapToFeepoundsModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeepoundsModel> getFeeByAmount(Double amount, Pageable pageable) {
        return feepoundsRepository.findByAmount(amount, pageable)
                .map(FeepoundsMapper::mapToFeepoundsModel);
    }

    @Override
    @Transactional(readOnly = true)
    public FeepoundsModel getFeeById(Long id) {
        Feepounds feepound = feepoundsRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Fee pounds not found with id: " + id));
        return FeepoundsMapper.mapToFeepoundsModel(feepound);
    }

    @Override
    public FeepoundsModel updateFee(Long id, FeepoundsModel feepoundsModel) {
        Feepounds feepound = feepoundsRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Fee pounds not found with id: " + id));

        feepound.setAmount(feepoundsModel.getAmount());
        Feepounds updated = feepoundsRepository.save(feepound);

        return FeepoundsMapper.mapToFeepoundsModel(updated);
    }

    @Override
    public void deleteFee(Long id) {
        Feepounds feepound = feepoundsRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Fee pounds not found with id: " + id));
        feepoundsRepository.delete(feepound);
    }
}
