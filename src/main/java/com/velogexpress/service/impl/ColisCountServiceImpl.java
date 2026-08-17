package com.velogexpress.service.impl;

import com.velogexpress.mapper.OrderDetailsMapper;
import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.repository.OrderDetailsRepository;
import com.velogexpress.service.ColisCounterService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ColisCountServiceImpl implements ColisCounterService {
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public Page<OrderDetailsModel> countDeliveredColis(String code, Pageable pageable) {
        return orderDetailsRepository.countDelevredColis(code, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> countShippedColis(String code, Pageable pageable) {
        return orderDetailsRepository.countShippedColis(code, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> countReadyColis(String code, Pageable pageable) {
        return orderDetailsRepository.countReadyColis(code, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }
}
