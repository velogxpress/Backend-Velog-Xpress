package com.velogexpress.service.impl;

import com.velogexpress.entity.Facture;
import com.velogexpress.entity.FactureDetails;
import com.velogexpress.entity.OrderDetails;
import com.velogexpress.mapper.FactureDetailsMapper;
import com.velogexpress.model.FactureDetailsModel;
import com.velogexpress.repository.FactureDetailsRepository;
import com.velogexpress.repository.FactureRepository;
import com.velogexpress.repository.OrderDetailsRepository;
import com.velogexpress.service.FactureDetailsService;
import com.velogexpress.tools.DateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class FactureDetailsServiceImpl implements FactureDetailsService {

    private final FactureDetailsRepository factureDetailsRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final FactureRepository factureRepository;

    @Override
    public FactureDetailsModel createFactureDetails(FactureDetailsModel model) {

        FactureDetails entity = FactureDetailsMapper.mapToFactureDetails(model);
        FactureDetails saved = factureDetailsRepository.save(entity);
        OrderDetails order = orderDetailsRepository.findByUpc(saved.getColis());

        if (order == null) {
            throw new IllegalStateException("OrderDetails introuvable pour UPC: " + saved.getColis());
        }
        order.setCondition("Payé");
        order.setStatus("Commande a été livrée.");
        order.setDelivery(DateTime.CURRENTDATE());
        orderDetailsRepository.save(order);
        return FactureDetailsMapper.mapToFactureDetailsModel(saved);
    }

    @Override
    public FactureDetailsModel createQuickFactureDetails(FactureDetailsModel factureDetailsModel) {
        FactureDetails entity = FactureDetailsMapper.mapToFactureDetails(factureDetailsModel);
        FactureDetails saved = factureDetailsRepository.save(entity);
        OrderDetails order = orderDetailsRepository.findByUpc(saved.getColis());
        Facture facture=factureRepository.findByCode(factureDetailsModel.getFacture().getCode());

        if (order == null) {
            throw new IllegalStateException("OrderDetails introuvable pour UPC: " + saved.getColis());
        }
        String stat=facture.getStatus();
        order.setCondition(stat);
        orderDetailsRepository.save(order);
        return FactureDetailsMapper.mapToFactureDetailsModel(saved);
    }

    @Override
    public Page<FactureDetailsModel> getAllFactureDetails(Pageable pageable) {
        return factureDetailsRepository.findAllByOrderByIdDesc(pageable)
                .map(FactureDetailsMapper::mapToFactureDetailsModel);
    }

    @Override
    public Page<FactureDetailsModel> getFactureDetails(String factureCode, Pageable pageable) {
        return factureDetailsRepository.findByFactureCode(factureCode, pageable)
                .map(FactureDetailsMapper::mapToFactureDetailsModel);
    }

    @Override
    public Optional<FactureDetailsModel> getSingleFactureDetails(String colis) {
        return Optional.ofNullable(factureDetailsRepository.findByColis(colis))
                .map(FactureDetailsMapper::mapToFactureDetailsModel);
    }

    @Override
    public void deleteFactureDetails(Long id) {
        log.debug("Deleting FactureDetails with ID: {}", id);
        factureDetailsRepository.deleteById(id);
    }
}
