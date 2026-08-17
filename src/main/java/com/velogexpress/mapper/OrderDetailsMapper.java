package com.velogexpress.mapper;

import com.velogexpress.entity.OrderDetails;
import com.velogexpress.model.CipinfeeModel;
import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.model.OrderModel;

public class OrderDetailsMapper {
    public static OrderDetailsModel mapToOrderDetailsModel(OrderDetails orderDetails){
        if (orderDetails == null) return null;
        return new OrderDetailsModel(
                orderDetails.getId(),
                orderDetails.getShip(),
                orderDetails.getClient(),
                orderDetails.getUpc(),
                orderDetails.getCategory(),
                orderDetails.getCitypoundfee(),
                orderDetails.getPounds(),
                orderDetails.getSubtotal(),
                orderDetails.getStatus(),
                orderDetails.getDelivery(),
                orderDetails.getExp_name(),
                orderDetails.getExp_email(),
                orderDetails.getExp_phone(),
                orderDetails.getRec_name(),
                orderDetails.getRec_email(),
                orderDetails.getRec_phone(),
                orderDetails.getType(),
                orderDetails.getCondition(),
                orderDetails.getPrice(),
                orderDetails.getTracking(),
                orderDetails.getDouane(),
                orderDetails.getPicture(),
                orderDetails.getNote(),
                orderDetails.getUser(),
                orderDetails.getCreatedAt()
        );
    }

    public static OrderDetails mapToOrderDetails(OrderDetailsModel orderDetailsModel){
        if (orderDetailsModel == null) return null;
        return new OrderDetails(
                orderDetailsModel.getId(),
                orderDetailsModel.getShip(),
                orderDetailsModel.getClient(),
                orderDetailsModel.getUpc(),
                orderDetailsModel.getCategory(),
                orderDetailsModel.getCitypoundfee(),
                orderDetailsModel.getPounds(),
                orderDetailsModel.getSubtotal(),
                orderDetailsModel.getStatus(),
                orderDetailsModel.getDelivery(),
                orderDetailsModel.getExp_name(),
                orderDetailsModel.getExp_email(),
                orderDetailsModel.getExp_phone(),
                orderDetailsModel.getRec_name(),
                orderDetailsModel.getRec_email(),
                orderDetailsModel.getRec_phone(),
                orderDetailsModel.getType(),
                orderDetailsModel.getCondition(),
                orderDetailsModel.getPrice(),
                orderDetailsModel.getTracking(),
                orderDetailsModel.getDouane(),
                orderDetailsModel.getPicture(),
                orderDetailsModel.getNote(),
                orderDetailsModel.getUser(),
                orderDetailsModel.getCreatedAt()
        );
    }
}
