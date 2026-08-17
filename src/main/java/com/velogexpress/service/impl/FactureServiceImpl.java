package com.velogexpress.service.impl;

import com.velogexpress.entity.Facture;
import com.velogexpress.entity.FactureDetails;
import com.velogexpress.entity.OrderDetails;
import com.velogexpress.entity.TempDetails;
import com.velogexpress.mapper.FactureMapper;
import com.velogexpress.model.FactureModel;
import com.velogexpress.print.CreateFacture;
import com.velogexpress.repository.*;
import com.velogexpress.service.FactureService;
import com.velogexpress.service.TempDetailsService;
import com.velogexpress.tools.DateTime;
import com.velogexpress.tools.DecimalFormat;
import com.velogexpress.tools.SKU;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.awt.print.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository;
    private final OrderDetailsRepository orderDetailsRepository;


    private final DateTime dt = new DateTime();
    private final SKU upc = new SKU();

    // ----------------- CREATE FACTURE -----------------
    @Override
    public FactureModel createFacture(FactureModel factureModel) {
        Facture facture = FactureMapper.mapToFacture(factureModel);
        facture.setCode(upc.FACTURECODE());
        facture.setDate(dt.CURRENTDATE());
        if(factureModel.getStatus().equals("Due")){
            if(factureModel.getEffectif() == null || factureModel.getEffectif() == 0){
                facture.setBalance(factureModel.getAmount()- factureModel.getDiscount());
            }else{
                facture.setBalance(factureModel.getAmount()-factureModel.getEffectif()-factureModel.getDiscount());
            }
        }
        // Save facture
        Facture savedFacture = factureRepository.save(facture);
        return FactureMapper.mapToFactureModel(savedFacture);
    }

    // ----------------- CREATE QUICK FACTURE -----------------
    @Override
    @Transactional
    public FactureModel createQuickFacture(FactureModel factureModel) {
        Facture facture = FactureMapper.mapToFacture(factureModel);
        facture.setCode(upc.FACTURECODE());
        facture.setDate(dt.CURRENTDATE());
        facture.setStatus("Payé");

        // Fetch order details
        Pageable pageable = Pageable.ofSize(25);
        Page<OrderDetails> detailsList = orderDetailsRepository.findDetailsByParameter(
                factureModel.getShip().getId(), factureModel.getClient(), pageable
        );

        double total = detailsList.getContent().stream()
                .mapToDouble(OrderDetails::getSubtotal)
                .sum();

        facture.setAmount(total + factureModel.getTarif());

        // Save facture
        Facture savedFacture = factureRepository.save(facture);

        // Optional: generate barcode
        generateBarcode(savedFacture.getCode());

        return FactureMapper.mapToFactureModel(savedFacture);
    }

    // ----------------- GET ALL FACTURES -----------------
    @Override
    public Page<FactureModel> getAllFacture(Pageable pageable) {
        return factureRepository.findAllByDesc(pageable)
                .map(FactureMapper::mapToFactureModel);
    }

    // ----------------- GET FACTURE BY CODE -----------------
    @Override
    public Optional<FactureModel> getFactureByCode(String code) {
        return Optional.ofNullable(factureRepository.findByCode(code))
                .map(FactureMapper::mapToFactureModel);
    }

    // ----------------- DELETE FACTURE -----------------
    @Override
    @Transactional
    public void deleteFacture(String code) {
        Optional<Facture> factureOpt = Optional.ofNullable(factureRepository.findByCode(code));
        if (factureOpt.isPresent()) {
            Facture facture = factureOpt.get();

            // Update related OrderDetails
            Pageable pageable = Pageable.ofSize(25);
            Page<OrderDetails> detailsList = orderDetailsRepository.findDetailsByParameter(
                    facture.getShip().getId(), facture.getClient(), pageable
            );

            detailsList.getContent().forEach(details -> {
                details.setDelivery("");
                details.setStatus("Commande prête à délivrer.");
                orderDetailsRepository.save(details);
            });

            // Delete facture
            factureRepository.delete(facture);
        }
    }

    // ----------------- PRINT FACTURE -----------------
    @Override
    public void printFacture() {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        paper.setSize(350, 842);
        paper.setImageableArea(0, 0, 350, 842);
        pf.setPaper(paper);
        pf.setOrientation(PageFormat.PORTRAIT);

        job.setPrintable(new CreateFacture(), pf);
        job.setJobName("Facture");

        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    // ----------------- SEARCH FACTURE -----------------
    @Override
    public Page<FactureModel> searchFacture(String code, Pageable pageable) {
        return factureRepository.searchFacture(code, pageable)
                .map(FactureMapper::mapToFactureModel);
    }

    // ----------------- GET FACTURE WITH DETAILS -----------------
    @Override
    public Page<FactureModel> getFactureDetailsWith(String client, Long order, Pageable pageable) {
        // Repository returns List due to JOIN FETCH
        Page<Facture> facturePage = factureRepository.findFactureWithDetails(client, order, pageable);
        Page<FactureModel> modelPage = facturePage
                .map(FactureMapper::mapToFactureModel);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), modelPage.getContent().size());

        return new PageImpl<>(modelPage.getContent().subList(start, end), pageable, modelPage.getContent().size());
    }

    // ----------------- UPDATE FACTURE STATUS -----------------
    @Override
    @Transactional
    public Optional<FactureModel> updateFacture(String code) {
        return Optional.ofNullable(factureRepository.findByCode(code))
                .map(facture -> {
                    facture.setStatus("Payé");
                    Facture updated = factureRepository.save(facture);
                    return FactureMapper.mapToFactureModel(updated);
                });
    }

    @Override
    public FactureModel getFactureToday() {
        String date=DateTime.CURRENTDATE();
        Facture facture = factureRepository.sumAmountToday(date);
        if (facture != null) {
            return FactureMapper.mapToFactureModel(facture);
        }else{
            return null;
        }

    }

    @Override
    public FactureModel getFactureToday(Long ID) {
        String date=DateTime.CURRENTDATE();
        Facture facture = factureRepository.sumAmountTodayFromMyCity(date,ID);
        if (facture != null) {
            return FactureMapper.mapToFactureModel(facture);
        }else{
            return null;
        }
    }

    @Override
    public Page<FactureModel> getCountAllFacture(Long order, Pageable pageable) {
        Page<Facture> facture=factureRepository.getFactureByOrder(order,pageable);
        if(facture==null){
            return null;
        }else{
            return facture.map(FactureMapper::mapToFactureModel);
        }

    }

    @Override
    public Page<FactureModel> getCountAllFactureBySurcursal(Long order, Long surcursal, Pageable pageable) {
        Page<Facture> facture=factureRepository.getFactureBySurcursal(order,surcursal,pageable);
        if(facture==null){
            return null;
        }else{
            return facture.map(FactureMapper::mapToFactureModel);
        }
    }

    // ----------------- PRIVATE HELPER -----------------
    private void generateBarcode(String code) {
        upc.BARCODEGENERATOR(code, 500, 200);
    }
}
