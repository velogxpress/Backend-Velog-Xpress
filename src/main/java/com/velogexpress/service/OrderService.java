package com.velogexpress.service;

import com.velogexpress.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.awt.print.PrinterException;
import java.time.LocalDate;

public interface OrderService {
    OrderModel createOrder();
    Page<OrderModel> getAllOrder(Pageable pageable);
    Page<OrderModel> getAllOrderCombo(Pageable pageable);
    Page<OrderModel> getOrderByShiporder(String upc, Pageable pageable);
    OrderModel getOrderByShiporderCode(String upc);
    OrderModel updateShipOrder(String upc,OrderModel orderModel) throws PrinterException;
    void deleteOrder(String upc);
    Long  countOrders();
    Long  countOrdersNow();
}
