package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.FactureModel;
import com.velogexpress.model.OrderDetailsModel;
import com.velogexpress.service.FactureService;
import com.velogexpress.service.OrderDetailsService;
import com.velogexpress.service.PdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/orderdetails")
@RequiredArgsConstructor
public class OrderDetailsController {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailsController.class);

    private final OrderDetailsService orderDetailsService;
    private final FactureService factureService;
    private final PdfService pdfService;

    // ---------------- BASIC CRUD ----------------

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PostMapping(
            value = "/save-colis",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<OrderDetailsModel> createOrderDetails(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("orderDetailModel") OrderDetailsModel orderDetailsModel
    ) {
        OrderDetailsModel saved =
                orderDetailsService.createDetails(file, orderDetailsModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping
    public Page<OrderDetailsModel> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        return orderDetailsService.getAllDetails(pageable);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/alldetails/{param}")
    public ResponseEntity<Page<OrderDetailsModel>> getAllOrderDetails(
        @PathVariable String param,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "25") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            return ResponseEntity.ok(orderDetailsService.getAllOrderDetails(param,pageable));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/etendu")
    public ResponseEntity<Page<OrderDetailsModel>> getAllOrderDetailsSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.getAllDetailsSearch(PageRequest.of(page, size)));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/searchetendu/{param}")
    public ResponseEntity<Page<OrderDetailsModel>> getAllOrderDetailsSearch(
            @PathVariable String param,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.searchAllDetails(param,PageRequest.of(page, size)));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/{upc}")
    public ResponseEntity<OrderDetailsModel> getDetailsByUpc(@PathVariable String upc) {
        OrderDetailsModel model = orderDetailsService.getDetailsByUPC(upc);
        return model != null ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PutMapping("/{upc}")
    public ResponseEntity<String> updateDetails(
            @PathVariable String upc,@RequestParam Long cityID) {
        String updated = orderDetailsService.updateDetailsStatus(upc,cityID);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @DeleteMapping("/{upc}")
    public ResponseEntity<String> deleteDetails(@PathVariable String upc) {
        orderDetailsService.deleteDetails(upc);
        return ResponseEntity.ok("Order details deleted successfully.");
    }

    // ---------------- SHIPPING ----------------

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/ship/{upc}")
    public ResponseEntity<Page<OrderDetailsModel>> getDetailsByShipOrder(
            @PathVariable String upc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.getByShipOrder(upc, PageRequest.of(page, size)));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/ships/{upc}")
    public ResponseEntity<List<OrderDetailsModel>> getDetailsByShipOrder(@PathVariable String upc) {
        return ResponseEntity.ok(orderDetailsService.getByShipOrder(upc));
    }


    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/ship/group/{upc}")
    public ResponseEntity<Page<OrderDetailsModel>> getDetailsGroupByShipOrder(
            @PathVariable String upc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.getByShipOrderGroup(upc, PageRequest.of(page, size)));
    }

    // ---------------- CITY ----------------

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/city/{id}")
    public ResponseEntity<Page<OrderDetailsModel>> getCityOrderDetails(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.showCityOrder(id, PageRequest.of(page, size)));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/city/{od}/{id}")
    public ResponseEntity<List<OrderDetailsModel>> getCityOrderDetailsByOrderAndCity(
            @PathVariable String od,
            @PathVariable Long id) {
        return ResponseEntity.ok(orderDetailsService.showCityOrderDetails(od, id));
    }

    // ---------------- FACTURE ----------------

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/facture/{id}/{od}")
    public ResponseEntity<Page<OrderDetailsModel>> getDetailsFacture(
            @PathVariable String id,
            @PathVariable String od,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.getDetailsFacture(id, od, PageRequest.of(page, size)));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/facture/client/{id}/{od}")
    public ResponseEntity<Page<OrderDetailsModel>> getDetailsByClientCode(
            @PathVariable String id,
            @PathVariable String od,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.getDetailsByClientCode(id, od, PageRequest.of(page, size)));
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PutMapping("/facture/update/{od}/{id}")
    public ResponseEntity<OrderDetailsModel> updateDetailsAfterFacture(
            @PathVariable String od,
            @PathVariable String id) {
        OrderDetailsModel updated = orderDetailsService.updateDetailsAfterFacture(od, id);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PostMapping("/facture/quick")
    public ResponseEntity<FactureModel> createQuickFacture(@RequestBody FactureModel factureModel) {
        FactureModel facture = factureService.createQuickFacture(factureModel);
        return new ResponseEntity<>(facture, HttpStatus.CREATED);
    }

    // ---------------- CLIENT ----------------

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/client/{usercode}")
    public ResponseEntity<Page<OrderDetailsModel>> getClientOrders(
            @PathVariable String usercode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.getByClient(usercode, PageRequest.of(page, size)));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/findclient/{usercode}")
    public ResponseEntity<Page<OrderDetailsModel>> getSearchClientOrders(
            @PathVariable String usercode,
            @RequestParam(defaultValue = "0") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.getSearchByClient(usercode,search,PageRequest.of(page, size)));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/trackcolis")
    public ResponseEntity<OrderDetailsModel> trackColis(@RequestParam() String search) {
        return ResponseEntity.ok(orderDetailsService.trackColis(search));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/client/search/{usercode}")
    public ResponseEntity<Page<OrderDetailsModel>> searchClientOrders(
            @PathVariable String usercode,
            @RequestParam String param,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.searchByClient(usercode, param, PageRequest.of(page, size)));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/client/searchs/{usercode}")
    public ResponseEntity<List<OrderDetailsModel>> searchClientOrders(@PathVariable String usercode,@RequestParam String param) {
        return ResponseEntity.ok(orderDetailsService.searchByClient(usercode, param));
    }

    // ---------------- SHIPPING STATS ----------------

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/shipping/count")
    public ResponseEntity<Page<OrderDetailsModel>> getShippingCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(orderDetailsService.getCountShipping(PageRequest.of(page, size)));
    }

    //Build download
    @GetMapping("/clentfacturedownload/{usercode}")
    public ResponseEntity<byte[]> generateClientfacturePdf(@PathVariable("usercode") String usercode,@RequestParam String search) {
        try {
            byte[] pdfBytes = pdfService.clientFactureDownload(usercode, search);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"colifacture.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            log.error("clentfacturedownload failed for usercode={}, search={}", usercode, search, e);
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(("Erreur facture: " + e.getMessage()).getBytes());
        }
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PutMapping("/updatedetails/{od}")
    public ResponseEntity<String> updateDetailsAfterReceiveOrder(@PathVariable String od) {
        String updated = orderDetailsService.updateDetails(od);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PutMapping("/updatecolis/{upc}")
    public ResponseEntity<OrderDetailsModel> updateDetailsColis(@PathVariable String upc,@RequestParam long id) {
        OrderDetailsModel model= orderDetailsService.updateColis(id,upc);
        return ResponseEntity.ok(model);
    }

    //Build download
    @GetMapping("/manifestdownload/{order}")
    public ResponseEntity<byte[]> generateManifestPdf(@PathVariable("order") String order,@RequestParam Long search) {
        try {
            byte[] pdfBytes = pdfService.manifestDownload(order, search);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"manifest.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();

        }
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/livraison/{order}")
    public ResponseEntity<List<OrderDetailsModel>> getCityOrderDetails(
            @PathVariable String order,
            @RequestParam Long cityID) {
        return ResponseEntity.ok(orderDetailsService.countOrderDetails(order,cityID));
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/livraisonsearch/{order}")
    public ResponseEntity<List<OrderDetailsModel>> getCityOrderDetails(
            @PathVariable String order,
            @RequestParam Long cityID,
            @RequestParam String search) {
        return ResponseEntity.ok(orderDetailsService.countOrderDetailsForSearch(order,cityID,search));
    }

    @GetMapping(
            value = "/labeldownload/{upc}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> generateLabelPdf(@PathVariable("upc") String upc) {

        byte[] pdfBytes = pdfService.labelDownload(upc);

        // 🔒 Sécurité : éviter réponse vide
        if (pdfBytes == null || pdfBytes.length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=label-" + upc + ".pdf")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/objectif")
    public ResponseEntity<OrderDetailsModel> getFactureAmount(){
        OrderDetailsModel model=orderDetailsService.getTotal();
        return ResponseEntity.ok(model);
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PostMapping
    public ResponseEntity<OrderDetailsModel> createSendOrderDetails(@RequestBody OrderDetailsModel orderDetailsModel) {
        OrderDetailsModel model=orderDetailsService.createSendDetails(orderDetailsModel);
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/graphecolisparville/{id}")
    public ResponseEntity<List> getColisVille(@PathVariable("id") Long id){
        List<OrderDetailsModel> model=orderDetailsService.grapheColisParVille(id);
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/grapheamountparville/{id}")
    public ResponseEntity<List> getAmountVille(@PathVariable("id") Long id){
        List<OrderDetailsModel> model=orderDetailsService.grapheAmountParVille(id);
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/objectifs/{cityID}")
    public ResponseEntity<OrderDetailsModel> getFactureAmount(@PathVariable("cityID") Long cityID){
        OrderDetailsModel model=orderDetailsService.getTotal(cityID);
        return ResponseEntity.ok(model);
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PutMapping("/actualiser/{id}")
    public ResponseEntity<OrderDetailsModel> updateOrderDetails(@PathVariable("id") Long id,@RequestBody OrderDetailsModel orderDetailsModel) {
        OrderDetailsModel model=orderDetailsService.updateOrderDetails(id, orderDetailsModel);
        return ResponseEntity.ok(model);
    }

    @CacheEvict(cacheNames = "orderDetails", allEntries = true)
    @PutMapping("/transfer/{id}")
    public ResponseEntity<OrderDetailsModel> transferOrderDetails(@PathVariable("id") Long id,@RequestParam("order_id") Long orderId) {
        OrderDetailsModel model=orderDetailsService.transferOrderDetails(id, orderId);
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/findclientinorder/{orderID}")
    public ResponseEntity<List> findClientInOrder(@PathVariable("orderID") Long orderID){
        List<OrderDetailsModel> model=orderDetailsService.findClientInOrder(orderID);
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/searchclientinorder/{orderID}")
    public ResponseEntity<List> searchClientInOrder(@PathVariable("orderID") Long orderID,@RequestParam String search){
        List<OrderDetailsModel> model=orderDetailsService.searchClientInOrder(orderID,search);
        return ResponseEntity.ok(model);
    }

    @Cacheable(cacheNames = "orderDetails")
    @GetMapping("/getclientinorder/{orderID}")
    public ResponseEntity<List> getClientInOrder(@PathVariable("orderID") Long orderID,@RequestParam String search){
        List<OrderDetailsModel> model=orderDetailsService.getClientInOrder(orderID,search);
        return ResponseEntity.ok(model);
    }

    @GetMapping(
            value = "/clientdownload/{upc}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> generateClientPdf(@PathVariable("upc") String upc) {

        byte[] pdfBytes = pdfService.ClientDownloadA4(upc);

        // 🔒 Sécurité : éviter réponse vide
        if (pdfBytes == null || pdfBytes.length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=colis-" + upc + ".pdf")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }
}
