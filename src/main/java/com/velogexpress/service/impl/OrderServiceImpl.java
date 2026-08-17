package com.velogexpress.service.impl;

import com.velogexpress.entity.Order;
import com.velogexpress.entity.OrderDetails;
import com.velogexpress.mapper.OrderMapper;
import com.velogexpress.model.OrderModel;
import com.velogexpress.repository.OrderDetailsRepository;
import com.velogexpress.repository.OrderRepository;
import com.velogexpress.service.EmailService;
import com.velogexpress.service.OrderService;
import com.velogexpress.tools.DateTime;
import com.velogexpress.tools.SKU;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final EmailService emailService;

    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final String STATUS_PENDING = "Détailles en attente.";
    private static final String STATUS_SHIPPED = "Commande expédiée.";

    /**
     * Create a new shipping order.
     */
    @Override
    public OrderModel createOrder() {
        SKU upc = new SKU();
        DateTime date = new DateTime();
        Order order = new  Order();
        order.setDate(date.CURRENTDATE());
        order.setShiporder(upc.SHIPORDERCODE());
        order.setColisQty(0);
        order.setPoundQty(0.0);
        order.setAmount(0.0);
        order.setStatus(STATUS_PENDING);

        Order saved = orderRepository.save(order);
        return OrderMapper.mapToOrderModel(saved);
    }

    /**
     * Get all orders with pagination.
     */
    @Override
    public Page<OrderModel> getAllOrder(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderMapper::mapToOrderModel);
    }

    @Override
    public Page<OrderModel> getAllOrderCombo(Pageable pageable) {
        return orderRepository.findByShiporderList(pageable)
                .map(OrderMapper::mapToOrderModel);
    }

    /**
     * Search orders by ship order code fragment.
     */
    @Override
    public Page<OrderModel> getOrderByShiporder(String upc, Pageable pageable) {
        return orderRepository.findByShiporderList(upc, pageable)
                .map(OrderMapper::mapToOrderModel);
    }

    /**
     * Get a specific order by its exact ship order code.
     */
    @Override
    public OrderModel getOrderByShiporderCode(String upc) {
        Order order = orderRepository.findByShiporder(upc);
        return (order != null) ? OrderMapper.mapToOrderModel(order) : null;
    }

    /**
     * Mark an order as shipped and notify all recipients by email.
     */
    @Transactional
    @Override
    public OrderModel updateShipOrder(String upc, OrderModel orderModel) {
        Pageable pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        Order order = orderRepository.findByShiporder(upc);

        if (order == null) return null;

        order.setStatus(orderModel.getStatus());
        order.setShipdate(orderModel.getShipdate());
        Order savedOrder = orderRepository.save(order);

        Page<OrderDetails> orderDetailsPage = orderDetailsRepository.findShipID(upc, pageable);
        if (orderDetailsPage == null || orderDetailsPage.isEmpty()) {
            return OrderMapper.mapToOrderModel(savedOrder);
        }

        // Update details + send notification emails
        for (OrderDetails detail : orderDetailsPage.getContent()) {
            String email = detail.getRec_email();
            if (email != null && !email.isBlank()) {
                String body ="Bonne nouvelle ! Votre colis est en chemin vers Haïti.<br>" +
                            " Vous pouvez suivre son voyage avec le numéro de suivi suivant :<br>" +
                             "<h3><strong>"+detail.getUpc()+"</strong></h3><br>"+
                            " Nous restons à votre disposition si vous avez des questions.<br>";
                emailService.sendMails(email, "Bonjour "+detail.getRec_name(), "Notification - " + order.getShiporder(),body);
            }
            detail.setStatus(STATUS_SHIPPED);
        }

        orderDetailsRepository.saveAll(orderDetailsPage.getContent());
        return OrderMapper.mapToOrderModel(savedOrder);
    }

    /**
     * Delete an order and all related order details.
     */
    @Transactional
    @Override
    public void deleteOrder(String upc) {
        Order order = orderRepository.findByShiporder(upc);
        if (order == null) return;

        Pageable pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        List<OrderDetails> detailsList = orderDetailsRepository.findByShip(order.getId(), pageable).getContent();

        if (!detailsList.isEmpty()) {
            orderDetailsRepository.deleteAll(detailsList);
        }

        orderRepository.delete(order);
    }

    @Override
    public Long countOrders() {

        int lastYear = LocalDate.now().minusYears(1).getYear();
        Long count = orderRepository.countColis(lastYear);
        return count;
    }

    @Override
    public Long countOrdersNow() {
        int thisYear = LocalDate.now().getYear();
        Long count = orderRepository.countColis(thisYear);
        return count;
    }
}
