package com.velogexpress.service.impl;

import com.velogexpress.entity.Agentsurcursal;
import com.velogexpress.mapper.AgentsurcursalMapper;
import com.velogexpress.model.AgentsurcursalModel;
import com.velogexpress.repository.AgentsurcursalRepository;
import com.velogexpress.service.AgentsurcursalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AgentsurcursalServiceImpl implements AgentsurcursalService {

    private final AgentsurcursalRepository repository;

    @Override
    public AgentsurcursalModel createAgentsurcursal(AgentsurcursalModel model) {
        Agentsurcursal entity = AgentsurcursalMapper.mapToAgentsurcursal(model);
        Agentsurcursal saved = repository.save(entity);
        log.info("Created Agentsurcursal with id: {}", saved.getId());
        return AgentsurcursalMapper.mapToAgentsurcursalModel(saved);
    }

    @Override
    public Page<AgentsurcursalModel> getAllAgentsurcursals(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AgentsurcursalMapper::mapToAgentsurcursalModel);
    }

    @Override
    public Page<AgentsurcursalModel> searchByUserCode(String userCode, Pageable pageable) {
        return repository.search(userCode, pageable)
                .map(AgentsurcursalMapper::mapToAgentsurcursalModel);
    }

    @Override
    public AgentsurcursalModel getByUserCode(String userCode) {
        return Optional.ofNullable(repository.findByUserCode(userCode))
                .map(AgentsurcursalMapper::mapToAgentsurcursalModel)
                .orElseThrow(() -> new RuntimeException("Agent not found for userCode: " + userCode));
    }

    @Override
    public AgentsurcursalModel updateByUserCode(String userCode, AgentsurcursalModel model) {
        Agentsurcursal existing = Optional.ofNullable(repository.findByUserCode(userCode))
                .orElseThrow(() -> new RuntimeException("Agent not found for userCode: " + userCode));

        // Update relevant fields
        if (model.getClient() != null) existing.setClient(model.getClient());
        if (model.getSurcursal() != null) existing.setSurcursal(model.getSurcursal());

        Agentsurcursal saved = repository.save(existing);
        log.info("Updated Agentsurcursal with userCode: {}", userCode);
        return AgentsurcursalMapper.mapToAgentsurcursalModel(saved);
    }

    @Override
    public void deleteByUserCode(String userCode) {
        Agentsurcursal existing = Optional.ofNullable(repository.findByUserCode(userCode))
                .orElseThrow(() -> new RuntimeException("Agent not found for userCode: " + userCode));
        repository.delete(existing);
        log.info("Deleted Agentsurcursal with userCode: {}", userCode);
    }
}
