package com.velogexpress.service.impl;

import com.velogexpress.entity.Specialfee;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.SpecialfeeMapper;
import com.velogexpress.model.SpecialfeeModel;
import com.velogexpress.repository.SpecialfeeRepository;
import com.velogexpress.service.SpecialfeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SpecialfeeServiceImpl implements SpecialfeeService {

    private final SpecialfeeRepository specialfeeRepository;

    @Override
    public SpecialfeeModel createSpecialfee(SpecialfeeModel specialfeeModel) {
        Specialfee specialfee = SpecialfeeMapper.mapToSpecialfee(specialfeeModel);
        Specialfee savedSpecialfee = specialfeeRepository.save(specialfee);
        return SpecialfeeMapper.mapToSpecialfeeModel(savedSpecialfee);
    }

    @Override
    public Page<SpecialfeeModel> getAllSpecialfee(Pageable pageable) {
        return specialfeeRepository.findAll(pageable)
                .map(SpecialfeeMapper::mapToSpecialfeeModel);
    }

    @Override
    public Page<SpecialfeeModel> getSpecialfeeByAmount(Double amount, Pageable pageable) {
        return specialfeeRepository.findByAmount(amount, pageable)
                .map(SpecialfeeMapper::mapToSpecialfeeModel);
    }

    @Override
    public SpecialfeeModel getSpecialfeeById(Long id) {
        Specialfee specialfee = findSpecialfeeOrThrow(id);
        return SpecialfeeMapper.mapToSpecialfeeModel(specialfee);
    }

    @Override
    public SpecialfeeModel updateSpecialfee(Long id, SpecialfeeModel specialfeeModel) {
        Specialfee specialfee = findSpecialfeeOrThrow(id);
        specialfee.setAmount(specialfeeModel.getAmount());
        Specialfee updatedSpecialfee = specialfeeRepository.save(specialfee);
        return SpecialfeeMapper.mapToSpecialfeeModel(updatedSpecialfee);
    }

    @Override
    public void deleteSpecialfee(Long id) {
        Specialfee specialfee = findSpecialfeeOrThrow(id);
        specialfeeRepository.delete(specialfee);
    }

    private Specialfee findSpecialfeeOrThrow(Long id) {
        return specialfeeRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Special fee not exists with given id " + id));
    }
}
