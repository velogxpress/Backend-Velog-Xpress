package com.velogexpress.service.impl;

import com.velogexpress.entity.TempDetails;
import com.velogexpress.mapper.TempDetailsMapper;
import com.velogexpress.model.TempDetailsModel;
import com.velogexpress.repository.TempDetailsRepository;
import com.velogexpress.service.TempDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TempDetailsServiceImpl implements TempDetailsService {

    private final TempDetailsRepository tempDetailsRepository;

    @Override
    public TempDetailsModel createFactureDetailsTMP(TempDetailsModel tempDetailsModel) {
        log.info("Creating TempDetails for client: {}", tempDetailsModel.getClient());
        TempDetails tempDetails = TempDetailsMapper.mapToTempDetails(tempDetailsModel);
        TempDetails saved = tempDetailsRepository.save(tempDetails);
        log.debug("TempDetails created with ID: {}", saved.getId());
        return TempDetailsMapper.mapToTempDetailsModel(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TempDetailsModel> getAllFactureDetailsTMP(String client, Pageable pageable) {
        log.info("Fetching all TempDetails for client: {}", client);
        return tempDetailsRepository.searchAllTMP(client, pageable)
                .map(TempDetailsMapper::mapToTempDetailsModel);
    }

    @Override
    @Transactional(readOnly = true)
    public TempDetailsModel getSingleFactureDetailsTMP(String factureID) {
        log.info("Fetching TempDetails for factureID: {}", factureID);
        return Optional.ofNullable(tempDetailsRepository.getFactureDetailsTMP(factureID))
                .map(TempDetailsMapper::mapToTempDetailsModel)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public TempDetailsModel getSingleDetailsTMP(Long id) {
        log.info("Fetching TempDetails by ID: {}", id);
        return Optional.ofNullable(tempDetailsRepository.getFactureByIDTMP(id))
                .map(TempDetailsMapper::mapToTempDetailsModel)
                .orElse(null);
    }

    @Override
    public TempDetailsModel updateFactureDetailsTMP(Long id, TempDetailsModel tempDetailsModel) {
        log.info("Updating TempDetails with ID: {}", id);

        TempDetails details = tempDetailsRepository.getFactureByIDTMP(id);
        if (details == null) {
            log.warn("TempDetails with ID {} not found", id);
            return null;
        }

        details.setDescription(tempDetailsModel.getDescription());
        details.setFee(tempDetailsModel.getFee());
        details.setFixedprice(tempDetailsModel.getFixedprice());
        details.setSoubtotal(tempDetailsModel.getSoubtotal());

        TempDetails updated = tempDetailsRepository.save(details);
        log.debug("TempDetails updated successfully for ID: {}", id);

        return TempDetailsMapper.mapToTempDetailsModel(updated);
    }

    @Override
    public void deleteFactureDetailsTMP(Long id) {
        log.info("Deleting TempDetails with ID: {}", id);

        TempDetails details = tempDetailsRepository.getFactureByIDTMP(id);
        if (details == null) {
            log.warn("No TempDetails found for ID {}", id);
            return;
        }

        tempDetailsRepository.delete(details);
        log.debug("TempDetails deleted for ID: {}", id);
    }

    @Override
    public void deleteAllFactureDetailsTMP(String client, Pageable pageable) {
        log.info("Deleting all TempDetails for client: {}", client);
        Page<TempDetails> details = tempDetailsRepository.getFactureByClientTMP(client, pageable);
        if (details.isEmpty()) {
            log.warn("No TempDetails found for client: {}", client);
            return;
        }

        tempDetailsRepository.deleteAll(details);
        log.debug("{} TempDetails deleted for client: {}", details.getContent().size(), client);
    }
}
