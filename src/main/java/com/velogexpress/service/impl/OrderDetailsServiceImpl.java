package com.velogexpress.service.impl;

import com.velogexpress.entity.Facture;
import com.velogexpress.entity.Order;
import com.velogexpress.entity.OrderDetails;
import com.velogexpress.mapper.FactureMapper;
import com.velogexpress.mapper.OrderDetailsMapper;
import com.velogexpress.model.FactureModel;
import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.repository.OrderDetailsRepository;
import com.velogexpress.repository.OrderRepository;
import com.velogexpress.repository.ClientRegisterRepository;
import com.velogexpress.repository.StorageDetailsRepository;
import com.velogexpress.repository.StoreRepository;
import com.velogexpress.service.*;
import com.velogexpress.tools.DateTime;
import com.velogexpress.tools.SKU;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final ClientRegisterRepository clientRegisterRepository;
    private final StorageDetailsRepository storageDetailsRepository;
    private final StoreRepository storeRepository;
    private final SurcursalService surcursalService;
    private final PdfService pdfService;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private NotificationService notificationService;

    @Value("${file.upload-dir}") private String uploadDir;

    @Autowired
    private EmailService emailService;

    private final SKU skuGenerator = new SKU();
    private final DateTime dateTime = new DateTime();

    private void RefreshOrder(OrderDetails orderdetails){
        RefreshOrder(orderdetails.getShip());
    }

    private void RefreshOrder(Order order){
        Order ship=orderRepository.findByShiporder(order.getShiporder());
        if(ship==null){
            return;
        }
        int qty= 0;
        double qtyPound=0;
        double amount=0;
        double var=0;
        String status="Detailles en attente.";
        List<OrderDetails> orderDetailsList=orderDetailsRepository.findByShipID(ship.getId());
        if(!orderDetailsList.isEmpty()){
           for(int i=0;i<orderDetailsList.size();i++){
               qty+=1;
               if(orderDetailsList.get(i).getPounds()==null || orderDetailsList.get(i).getPounds()==0){
                   qtyPound+=var;
               }else{
                   qtyPound+=orderDetailsList.get(i).getPounds();
               }
               amount+=orderDetailsList.get(i).getSubtotal();
           }
        }

        if(qty>0){
            status="Expédition en attente.";
        }

        ship.setColisQty(qty);
        ship.setPoundQty(qtyPound);
        ship.setAmount(amount);
        ship.setStatus(status);
        orderRepository.save(ship);
    }

    // ------------------- CREATE -------------------
    @Override
    public OrderDetailsModel createDetails(MultipartFile file, OrderDetailsModel model) {
        OrderDetails details = OrderDetailsMapper.mapToOrderDetails(model);
        // 1️⃣ Nom original nettoyé
        String cleanOriginalName = Paths
                .get(Objects.requireNonNull(file.getOriginalFilename()))
                .getFileName()
                .toString();

        String extension = "";
        if (cleanOriginalName.contains(".")) {
            extension = cleanOriginalName.substring(cleanOriginalName.lastIndexOf("."));
        }

        // 2️⃣ Nom temporaire SAFE
        String tempFileName =
                "upload_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        // 3️⃣ Chemin temporaire
        String tempFilePath =
                System.getProperty("java.io.tmpdir") + File.separator + tempFileName;

        // 4️⃣ Transfer vers temp
        File tempFile = new File(tempFilePath);
        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Erreur transfert fichier", e);
        }

        // 5️⃣ Dossier final
        File uploadPath = new File(uploadDir + "/products/");
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        // 6️⃣ Nouveau nom FINAL
        String finalFileName =
                System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        try {
            BufferedImage bufferedImage = ImageIO.read(tempFile);

            File outputFile = new File(uploadPath, finalFileName);
            ImageIO.write(bufferedImage, extension.replace(".", ""), outputFile);


            // 7️⃣ Entity Image
            details.setPicture(finalFileName);
            details.setUpc(skuGenerator.COLISCODE());

            if(model.getType().equals("Indirecte"))
            {
                details.setClient(null);
            }
            // 8️⃣ Save
            OrderDetails saved = orderDetailsRepository.save(details);

            // 9️⃣ Nettoyage temp
            tempFile.delete();
            List<String> piecesJointe=null;
            piecesJointe=List.of(
                    pdfService.create80PdfMove(saved,uploadDir+"/products/"),
                    pdfService.create80PdfMoves(saved,uploadDir+"/products/"),
                    uploadDir+"/products/"+saved.getPicture()
            );
            List<String> piecesJointeName=null;
            piecesJointeName=List.of(
                    saved.getRec_phone()+"_"+saved.getShip().getShiporder()+".pdf",
                    saved.getPicture()
            );
            String body="Nous avons reçu un colis pour votre compte."
                    + "Restez connecté avec nous pour d’autres notifications."
                    + "Vous pouvez suivre le colis sur notre application ou notre site web avec votre code de suivi: "+saved.getUpc()
                    + " \n\nLa date limite pour recevoir ce colis est le: "+DateTime.ADDDAYSTODATE( 10);
            if(saved.getRec_email()!=null){
                notificationService.sendPushNotification(saved.getRec_email(),"Reception de colis",body);
            }

            sendNotificationEmail(saved,piecesJointeName);
            RefreshOrder(saved);
            return OrderDetailsMapper.mapToOrderDetailsModel(saved);

        } catch (IOException e) {
            throw new RuntimeException("Erreur sauvegarde image", e);
        }
    }



    @Override
    public Page<OrderDetailsModel> getAllOrderDetails(String param, Pageable pageable) {
        Page<OrderDetails> orderDetailsList = orderDetailsRepository.findAllByParam(param,pageable);
        return  orderDetailsList.map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    private void sendNotificationEmail(OrderDetails details,List<String> files) {
        String body="Nous avons reçu un colis pour votre compte.<br>"
                + "Restez connecté avec nous pour d’autres notifications.<br>"
                + "Vous pouvez suivre le colis sur notre application ou notre site web avec votre code de suivi: "+details.getUpc()+"<br>"
                + "La date limite pour recevoir ce colis est le: "+DateTime.ADDDAYSTODATE( 10);

        if(details.getRec_email() != null) {
            //emailService.sendMailWithAttachments(details.getRec_email(),"Bonjour "+details.getRec_name() , "Notification",body,files);
            emailService.sendMailWithDownloadLinks(details.getRec_email(),"Bonjour "+details.getRec_name() , "Notification",body,files);
        }
    }

    private void sendNotificationEmail(OrderDetails details) {
        String body="Nous avons reçu un colis pour votre compte.<br>"
                + "Restez connecté avec nous pour d’autres notifications.<br>"
                + "Vous pouvez suivre le colis sur notre site web avec votre code de suivi: "+details.getUpc()+"<br>"
                + "La date limite pour recevoir ce colis est le: "+DateTime.ADDDAYSTODATE( 10);

        if(details.getRec_email() != null) {
            emailService.sendMails(details.getRec_email(),"Bonjour "+details.getRec_name() , "Notification",body);
        }
    }

    // ------------------- GET -------------------
    @Override
    public Page<OrderDetailsModel> getAllDetails(Pageable pageable) {
        return orderDetailsRepository.findAll(pageable).map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public OrderDetailsModel getDetailsByUPC(String upc) {
        return Optional.ofNullable(orderDetailsRepository.findByUpc(upc))
                .map(OrderDetailsMapper::mapToOrderDetailsModel)
                .orElse(null);
    }

    @Override
    public Page<OrderDetailsModel> getDetailsByShipOrder(Long shipId, Pageable pageable) {
        return orderDetailsRepository.findByShip(shipId, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getByShipOrder(String shipId, Pageable pageable) {
        return orderDetailsRepository.findShipID(shipId, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public List<OrderDetailsModel> getByShipOrder(String up) {
        List<OrderDetails> modelList=orderDetailsRepository.finddShipID(up);
        return modelList.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
    }

    @Override
    public Page<OrderDetailsModel> getByShipOrderGroup(String shipId, Pageable pageable) {
        return orderDetailsRepository.findShipIDGroup(shipId, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getDetailsByClientCode(String clientId, String orderId, Pageable pageable) {
        return orderDetailsRepository.findClientIDForFacture(clientId, orderId, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getOrderInFacture(String clientId, Pageable pageable) {
        return orderDetailsRepository.findClientIDForFacture(clientId, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getDetailsFacture(String clientId, String orderId, Pageable pageable) {
        return orderDetailsRepository.searchDetailsFacture(clientId, orderId, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getQuickDetailsFacture(String clientId, String orderId, Pageable pageable) {
        return orderDetailsRepository.findQuickFacture(clientId, orderId, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getDetailsColis(String clientId, Pageable pageable) {
        return orderDetailsRepository.findColis(clientId, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getCountShipping(Pageable pageable) {
        return orderDetailsRepository.countShipping(pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getByClient(String client, Pageable pageable) {
        return orderDetailsRepository.getOrderDetailByClient(client, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> getSearchByClient(String client, String search, Pageable pageable) {
        return orderDetailsRepository.getOrderDetailSearchByClient(client,search, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> searchByClient(String client, String param, Pageable pageable) {
        return orderDetailsRepository.searchOrderDetailByClient(client, param, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public List<OrderDetailsModel> searchByClient(String client, String param) {
        List<OrderDetails> detailsList=orderDetailsRepository.searchsOrderDetailByClient(client, param);
        if(detailsList==null) {
            return null;
        }else{
            return detailsList.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
        }
    }

    @Override
    public OrderDetailsModel trackColis(String param) {
        OrderDetails model=orderDetailsRepository.searchColis(param);
        if(model==null){
            return null;
        }
        return OrderDetailsMapper.mapToOrderDetailsModel(model);
    }

    @Override
    public OrderDetailsModel searchExpediteur(String phone) {
        return Optional.ofNullable(orderDetailsRepository.findExpediteur(phone))
                .map(OrderDetailsMapper::mapToOrderDetailsModel)
                .orElse(null);
    }

    @Override
    public OrderDetailsModel searchReceiver(String phone) {
        return Optional.ofNullable(orderDetailsRepository.findReceiver(phone))
                .map(OrderDetailsMapper::mapToOrderDetailsModel)
                .orElse(null);
    }

    @Override
    public Page<OrderDetailsModel> showCityOrder(String order, Pageable pageable) {
        return orderDetailsRepository.searchCity(order, pageable)
                .map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public List<OrderDetailsModel> showCityOrderDetails(String order, Long city) {
        List <OrderDetails> details= orderDetailsRepository.findShipIDCity(order, city);
        if(details==null) return null;
        return details.stream().map(
                OrderDetailsMapper::mapToOrderDetailsModel
        ).collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailsModel> countOrderDetails(String order, Long city) {
      List<OrderDetails> model=orderDetailsRepository.countDetailsForFacture(order,city);
      if(model==null) return null;
      return model.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailsModel> countOrderDetailsForSearch(String order, Long city, String search) {
        List<OrderDetails> model=orderDetailsRepository.countDetailsForsearch(order,city,search);
        if(model==null) return null;
        return model.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
    }

    @Override
    public OrderDetailsModel updateColis(Long id,String upc) {
        OrderDetails detail=orderDetailsRepository.findReceivers(id,upc);
        if (detail == null) {
            throw new IllegalStateException("OrderDetails introuvable pour telephone: " + upc);
        }
        detail.setStatus("Commande a été livrée.");
        detail.setDelivery(DateTime.CURRENTDATE());
        OrderDetails update=orderDetailsRepository.save(detail);
        if(detail.getRec_email()!=null){
            notificationService.sendPushNotification(detail.getRec_email(),"Livraison",detail.getRec_name()+" votre commande a été livrée avec succes. ");
        }

        return OrderDetailsMapper.mapToOrderDetailsModel(update);
    }

    @Override
    public Page<OrderDetailsModel> getAllDetailsSearch(Pageable pageable) {
        return orderDetailsRepository.findAllDesc(pageable).map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    @Override
    public Page<OrderDetailsModel> searchAllDetails(String param, Pageable pageable) {
        return orderDetailsRepository.findForColis(param,pageable).map(OrderDetailsMapper::mapToOrderDetailsModel);
    }

    // ------------------- UPDATE -------------------
    @Override
    public String updateDetails(String upc) {
        List<OrderDetails> details = orderDetailsRepository.updateDetails(upc);

        if (details == null) return "Failed to update details";
        for (OrderDetails detail : details) {
            detail.setStatus("Commande bien arrivée en Haïti.");
            orderDetailsRepository.save(detail);
        }

        return "Success!";
    }

    @Override
    public String updateDetailsStatus(String upc, Long cityID) {
        OrderDetails details=orderDetailsRepository.updateDetailsStatus(upc);
        if (details==null) return "Failed to update details";

        Long orderID=details.getShip().getId();
        String clientName=details.getRec_name();
        String clientPhone=details.getRec_phone();

        if(details.getCitypoundfee().getCity().getId().equals(cityID)) {
            List<OrderDetails> detailsList=orderDetailsRepository.findOrderDetails(orderID,clientName,clientPhone);
            for(OrderDetails detail : detailsList) {
                detail.setStatus("Commande prête à être livrée.");
                orderDetailsRepository.save(detail);
            }

            String zone = details.getCitypoundfee().getCity().getDescription();
            var surcursal = surcursalService.getSurcursalByVille(zone, Pageable.ofSize(1)).getContent().get(0);

            OrderDetails mailDetails=orderDetailsRepository.findDataMessage(orderID,clientName,clientPhone,cityID);


            String body ="Nous avons recu "+mailDetails.getUpc()+" colis pour votre compte, le poids total est de "+mailDetails.getPounds()+" lbs.<br>"
                    +"Vos colis sont disponibles pour retrait à l’adresse suivante :<br>"
                    + surcursal.getAddress() + ", " + surcursal.getVille().getDescription()
                    + "<br> Horaires: " + surcursal.getHoraire()
                    + "<br> Merci de présenter une pièce d’identité et votre numéro de commande : " + details.getShip().getShiporder();

            String sbody ="Nous avons recu "+mailDetails.getUpc()+" colis pour votre compte, le poids total est de "+mailDetails.getPounds()+" lbs.\n"
                    +"Vos colis sont disponibles pour retrait à l’adresse suivante :\n"
                    + surcursal.getAddress() + ", " + surcursal.getVille().getDescription()
                    + "\n Horaires: " + surcursal.getHoraire()
                    + "\n Merci de présenter une pièce d’identité et votre numéro de commande : " + details.getShip().getShiporder();

            String bodys ="Nous avons recu "+mailDetails.getUpc()+" colis pour votre compte, le poids total est de "+mailDetails.getPounds()+" lbs.\n"
                    +"Vos colis sont disponibles pour retrait à l’adresse suivante :\n"
                    + surcursal.getAddress() + ", " + surcursal.getVille().getDescription()
                    + "\nHoraires: " + surcursal.getHoraire()
                    + "\nMerci de présenter une pièce d’identité et votre numéro de commande : " + details.getShip().getShiporder();

            if(details.getRec_email()!=null) {
                notificationService.sendPushNotification(details.getRec_email(),"Commande Disponible",sbody);
                emailService.sendMails(details.getRec_email(),"Bonjour " + details.getRec_name(),"Commande Disponible",body);
            }
           return bodys;
        }else{
            return "Wrong city";
        }
    }

    @Override
    public OrderDetailsModel updateDetailsAfterFacture(String order, String client) {
        Pageable pageable = PageRequest.of(0, 25);
        Page<OrderDetails> detailsPage = orderDetailsRepository.findByOrderClient(order, client, pageable);
        OrderDetails lastUpdated = null;

        if (detailsPage != null && !detailsPage.getContent().isEmpty()) {
            for (OrderDetails details : detailsPage.getContent()) {
                details.setStatus("Colis Délivré avec Succès.");
                details.setDelivery(dateTime.CURRENTDATETIME());
                lastUpdated = orderDetailsRepository.save(details);
            }
            if(lastUpdated!=null){
                notificationService.sendPushNotification(lastUpdated.getRec_email(),"Livraison",lastUpdated.getRec_name()+" votre commande a été livrée avec succes. ");
            }

        }

        return lastUpdated != null ? OrderDetailsMapper.mapToOrderDetailsModel(lastUpdated) : null;
    }



    // ------------------- DELETE -------------------
    @Override
    public void deleteDetails(String upc) {
        OrderDetails details = orderDetailsRepository.findByUpc(upc);
        if (details == null) return;

        storageDetailsRepository.deleteByOrderDetails(details.getId());
        storeRepository.deleteByOrderDetails(details.getId());
        jdbcTemplate.update("DELETE FROM order_details_photos WHERE orderdetails_id=?", details.getId());
        orderDetailsRepository.delete(details);
        RefreshOrder(details);
    }


    // ------------------- PRINT -------------------
    @Override
    public OrderDetailsModel printLabel(String upc) {
        return Optional.ofNullable(orderDetailsRepository.findByUpc(upc))
                .map(OrderDetailsMapper::mapToOrderDetailsModel)
                .orElse(null);
    }

    @Override
    public OrderDetailsModel getTotal() {
        OrderDetails facture=orderDetailsRepository.sumAmountFacture();
        return OrderDetailsMapper.mapToOrderDetailsModel(facture);
    }

    @Override
    public OrderDetailsModel getTotal(Long cityID) {
        OrderDetails facture=orderDetailsRepository.sumAmountFactureFromMyCity(cityID);
        return OrderDetailsMapper.mapToOrderDetailsModel(facture);
    }

    @Override
    public OrderDetailsModel createSendDetails(OrderDetailsModel orderDetailsModel) {
        OrderDetails orderDetails = OrderDetailsMapper.mapToOrderDetails(orderDetailsModel);
        orderDetails.setUpc(skuGenerator.COLISCODE());
        if(orderDetailsModel.getType().equals("Indirecte")){
            orderDetails.setClient(null);
        }else{
            orderDetails.setClient(orderDetailsModel.getClient());
        }
        OrderDetails saveObj = orderDetailsRepository.save(orderDetails);
        RefreshOrder(saveObj);
        String body="Nous avons reçu un colis pour votre compte."
                + "Restez connecté avec nous pour d’autres notifications."
                + "Vous pouvez suivre le colis sur notre application ou notre site web avec votre code de suivi: "+saveObj.getUpc()
                + "\n\nLa date limite pour recevoir ce colis est le: "+DateTime.ADDDAYSTODATE( 10);
        if(saveObj.getRec_email()!=null){
            notificationService.sendPushNotification(saveObj.getRec_email(),"Reception de colis",body);
        }
        notificationService.sendPushNotification(saveObj.getRec_email(),"Reception de colis",body);
        sendNotificationEmail(saveObj);
        return OrderDetailsMapper.mapToOrderDetailsModel(saveObj);
    }

    @Override
    public List<OrderDetailsModel> grapheColisParVille(Long orderID) {
        List<OrderDetails> detailsList=orderDetailsRepository.countOrderDetailsColisParVille(orderID);
        if(detailsList!=null){
            return detailsList.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
        }else{
            return List.of();
        }

    }

    @Override
    public List<OrderDetailsModel> grapheAmountParVille(Long orderID) {
        List<OrderDetails> detailsList=orderDetailsRepository.countOrderDetailsAmountParVille(orderID);
        if(detailsList!=null){
            return detailsList.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
        }else{
            return List.of();
        }
    }

    @Override
    public OrderDetailsModel updateOrderDetails(Long id, OrderDetailsModel orderDetailsModel) {
        OrderDetails orderDetails = orderDetailsRepository.findById(id).orElse(null);
        if (orderDetails == null) {
            return null;
        }else{
            orderDetails.setType(orderDetailsModel.getType());
            orderDetails.setPrice(orderDetailsModel.getPrice());
            orderDetails.setCitypoundfee(orderDetailsModel.getCitypoundfee());
            if(orderDetailsModel.getType().equals("Indirecte")){
                orderDetails.setClient(null);
            }else if (orderDetailsModel.getClient() != null && orderDetailsModel.getClient().getId() != null) {
                orderDetails.setClient(clientRegisterRepository
                        .findById(orderDetailsModel.getClient().getId())
                        .orElse(null));
            }else{
                orderDetails.setClient(null);
            }
            orderDetails.setExp_name(orderDetailsModel.getExp_name());
            orderDetails.setExp_phone(orderDetailsModel.getExp_phone());
            orderDetails.setExp_email(orderDetailsModel.getExp_email());
            orderDetails.setRec_email(orderDetailsModel.getRec_email());
            orderDetails.setRec_phone(orderDetailsModel.getRec_phone());
            orderDetails.setRec_name(orderDetailsModel.getRec_name());
            orderDetails.setPounds(orderDetailsModel.getPounds());
            orderDetails.setSubtotal(orderDetailsModel.getSubtotal());
            orderDetails.setPrice(orderDetailsModel.getPrice());
            orderDetails.setDouane(orderDetailsModel.getDouane());
            orderDetails.setNote(orderDetailsModel.getNote());
            orderDetails.setCategory(orderDetailsModel.getCategory());
            OrderDetails saveObj = orderDetailsRepository.save(orderDetails);
            RefreshOrder(saveObj);
            return OrderDetailsMapper.mapToOrderDetailsModel(saveObj);
        }
    }

    @Override
    public OrderDetailsModel transferOrderDetails(Long id, Long orderId) {
        OrderDetails orderDetails = orderDetailsRepository.findById(id).orElse(null);
        Order destinationOrder = orderRepository.findById(orderId).orElse(null);

        if (orderDetails == null || destinationOrder == null) {
            return null;
        }

        Order previousOrder = orderDetails.getShip();
        orderDetails.setShip(destinationOrder);

        OrderDetails saveObj = orderDetailsRepository.save(orderDetails);
        RefreshOrder(previousOrder);
        RefreshOrder(destinationOrder);

        return OrderDetailsMapper.mapToOrderDetailsModel(saveObj);
    }

    @Override
    public List<OrderDetailsModel> findClientInOrder(Long orderID) {
        List<OrderDetails> detailsList=orderDetailsRepository.findReceiverInOrderDetails(orderID);
        if(detailsList!=null){
            return detailsList.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
        }else{
            return List.of();
        }
    }

    @Override
    public List<OrderDetailsModel> searchClientInOrder(Long orderID, String search) {
        List<OrderDetails> detailsList=orderDetailsRepository.searchReceiverInOrderDetails(orderID, search);
        if(detailsList!=null){
            return detailsList.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
        }else{
            return List.of();
        }
    }

    @Override
    public List<OrderDetailsModel> getClientInOrder(Long orderID, String search) {
        List<OrderDetails> detailsList=orderDetailsRepository.searchReceiverInOrderDetailsForFacture(orderID, search);
        if(detailsList!=null){
            return detailsList.stream().map(OrderDetailsMapper::mapToOrderDetailsModel).collect(Collectors.toList());
        }else{
            return List.of();
        }
    }
}
