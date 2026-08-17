package com.velogexpress.service;

import com.velogexpress.model.FactureModel;
import com.velogexpress.model.OrderDetailsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrderDetailsService {
    OrderDetailsModel createDetails(MultipartFile file,OrderDetailsModel orderDetailsModel);
    Page<OrderDetailsModel> getAllOrderDetails(String param,Pageable pageable);
    Page<OrderDetailsModel> getAllDetails(Pageable pageable);
    OrderDetailsModel getDetailsByUPC(String upc);
    Page<OrderDetailsModel> getDetailsByShipOrder(Long Id, Pageable pageable);
    Page<OrderDetailsModel> getByShipOrder(String up, Pageable pageable);
    List<OrderDetailsModel> getByShipOrder(String up);
    Page<OrderDetailsModel> getByShipOrderGroup(String up, Pageable pageable);
    String updateDetails(String upc);
    String updateDetailsStatus(String upc,Long cityID);
    OrderDetailsModel updateDetailsAfterFacture(String order,String client);
    void deleteDetails(String upc);
    OrderDetailsModel printLabel(String upc);
    Page<OrderDetailsModel> getDetailsByClientCode(String Id,String Od, Pageable pageable);
    Page<OrderDetailsModel> getOrderInFacture(String Id, Pageable pageable);
    Page<OrderDetailsModel> getDetailsFacture(String Id,String Od, Pageable pageable);
    Page<OrderDetailsModel> getQuickDetailsFacture(String Id,String Od, Pageable pageable);
    Page<OrderDetailsModel> getDetailsColis(String Id, Pageable pageable);
    Page<OrderDetailsModel> getCountShipping(Pageable pageable);
    Page<OrderDetailsModel> getByClient(String client, Pageable pageable);
    Page<OrderDetailsModel> getSearchByClient(String client,String search, Pageable pageable);
    Page<OrderDetailsModel> searchByClient(String client,String param, Pageable pageable);
    List<OrderDetailsModel> searchByClient(String client,String param);
    OrderDetailsModel trackColis(String param);
    OrderDetailsModel searchExpediteur(String phone);
    OrderDetailsModel searchReceiver(String phone);
    Page<OrderDetailsModel> showCityOrder(String order, Pageable pageable);
    List<OrderDetailsModel> showCityOrderDetails(String order,Long city);
    List<OrderDetailsModel> countOrderDetails(String order,Long city);
    List<OrderDetailsModel> countOrderDetailsForSearch(String order,Long city,String search);
    OrderDetailsModel updateColis(Long id,String upc);
    Page<OrderDetailsModel> getAllDetailsSearch(Pageable pageable);
    Page<OrderDetailsModel> searchAllDetails(String param,Pageable pageable);
    OrderDetailsModel getTotal();
    OrderDetailsModel getTotal(Long cityID);
    OrderDetailsModel createSendDetails(OrderDetailsModel orderDetailsModel);
    List<OrderDetailsModel> grapheColisParVille(Long orderID);
    List<OrderDetailsModel> grapheAmountParVille(Long orderID);
    OrderDetailsModel updateOrderDetails(Long id,OrderDetailsModel orderDetailsModel);
    OrderDetailsModel transferOrderDetails(Long id, Long orderId);
    List<OrderDetailsModel> findClientInOrder(Long orderID);
    List<OrderDetailsModel> searchClientInOrder(Long orderID,String search);
    List<OrderDetailsModel> getClientInOrder(Long orderID,String search);
}
