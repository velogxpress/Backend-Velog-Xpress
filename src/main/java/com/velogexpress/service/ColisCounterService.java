package com.velogexpress.service;

import com.velogexpress.model.OrderDetailsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ColisCounterService {
    Page<OrderDetailsModel> countDeliveredColis(String code, Pageable pageable);
    Page<OrderDetailsModel> countShippedColis(String code, Pageable pageable);
    Page<OrderDetailsModel> countReadyColis(String code, Pageable pageable);
}
