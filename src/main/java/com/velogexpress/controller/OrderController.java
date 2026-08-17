package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.OrderModel;
import com.velogexpress.service.OrderService;
import com.velogexpress.service.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PrinterException;

@Slf4j
@RestController
@RequiredArgsConstructor
//@CrossOrigin("*")
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final PdfService  pdfService;

    /**
     * Create a new order.
     */
    @CacheEvict(cacheNames = "order", allEntries = true)
    @PostMapping
    public ResponseEntity<OrderModel> createOrder() {
        OrderModel created = orderService.createOrder();
        return ResponseEntity.status(201).body(created);
    }

    /**
     * Retrieve all orders with pagination.
     */
    @Cacheable(cacheNames = "order")
    @GetMapping
    public ResponseEntity<Page<OrderModel>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<OrderModel> orders = orderService.getAllOrder(PageRequest.of(page, size));
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieve all orders with pagination.
     */
    @Cacheable(cacheNames = "order")
    @GetMapping("/combo")
    public ResponseEntity<Page<OrderModel>> getAllOrdersCombo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<OrderModel> orders = orderService.getAllOrderCombo(PageRequest.of(page, size));
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieve an order by its shipment code.
     */
    @Cacheable(cacheNames = "order")
    @GetMapping("/{upc}")
    public ResponseEntity<?> getOrderByShipOrder(@PathVariable String upc) {
        OrderModel model = orderService.getOrderByShiporderCode(upc);
        if (model == null) {
            return ResponseEntity.status(404)
                    .body("Order not found for code: " + upc);
        }
        return ResponseEntity.ok(model);
    }

    /**
     * Update an order by its shipment code.
     */
    @CacheEvict(cacheNames = "order", allEntries = true)
    @PutMapping("/{upc}")
    public ResponseEntity<?> updateShipOrder(
            @PathVariable String upc,
            @RequestBody OrderModel orderModel
    ) {
        try {
            OrderModel updated = orderService.updateShipOrder(upc, orderModel);
            if (updated == null) {
                return ResponseEntity.status(404)
                        .body("Unable to update. Order not found for code: " + upc);
            }
            return ResponseEntity.ok(updated);
        } catch (PrinterException e) {
            log.error("Error printing order {}: {}", upc, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Printing error occurred while updating order " + upc);
        }
    }

    /**
     * Delete an order by its shipment code.
     */
    @CacheEvict(cacheNames = "order", allEntries = true)
    @DeleteMapping("/{upc}")
    public ResponseEntity<String> deleteOrder(@PathVariable String upc) {
        orderService.deleteOrder(upc);
        return ResponseEntity.ok("Order deleted successfully.");
    }

    /**
     * Search orders by shipment code with pagination.
     */
    @Cacheable(cacheNames = "order")
    @GetMapping("/search/{upc}")
    public ResponseEntity<Page<OrderModel>> searchOrdersByCode(
            @PathVariable String upc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<OrderModel> orders = orderService.getOrderByShiporder(upc, PageRequest.of(page, size));
        return ResponseEntity.ok(orders);
    }

    @Cacheable(cacheNames = "order")
    @GetMapping("/countcolis")
    public ResponseEntity<Long> countOrder(){
        Long counted=orderService.countOrders();
        return ResponseEntity.ok(counted);
    }

    @Cacheable(cacheNames = "order")
    @GetMapping("/countcolisnow")
    public ResponseEntity<Long> countOrderNow(){
        Long counted=orderService.countOrdersNow();
        return ResponseEntity.ok(counted);
    }

    @GetMapping(
            value = "/rapportdownload/{upc}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> generateRapportPdf(@PathVariable("upc") String upc) {

        byte[] pdfBytes = pdfService.rapportDownload(upc);

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

}
