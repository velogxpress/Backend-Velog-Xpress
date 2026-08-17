package com.velogexpress.mapper;

import com.velogexpress.entity.Order;
import com.velogexpress.model.OrderModel;

public class OrderMapper {
    public static OrderModel mapToOrderModel(Order order){
        return new OrderModel(
                order.getId(),
                order.getDate(),
                order.getShiporder(),
                order.getColisQty(),
                order.getPoundQty(),
                order.getAmount(),
                order.getStatus(),
                order.getShipdate()
        );
    }

    public static  Order mapToOrder(OrderModel orderModel){
        return new Order(
                orderModel.getId(),
                orderModel.getDate(),
                orderModel.getShiporder(),
                orderModel.getColisQty(),
                orderModel.getPoundQty(),
                orderModel.getAmount(),
                orderModel.getStatus(),
                orderModel.getShipdate()
        );
    }
}
