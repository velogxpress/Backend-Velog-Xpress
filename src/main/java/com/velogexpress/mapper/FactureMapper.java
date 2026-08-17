package com.velogexpress.mapper;

import com.velogexpress.entity.Facture;
import com.velogexpress.model.FactureModel;


public class FactureMapper {
    public static FactureModel mapToFactureModel(Facture facture){
        if(facture==null){
            return null;
        }
        return new FactureModel(
                facture.getId(),
                facture.getCode(),
                facture.getDate(),
                facture.getClient(),
                facture.getClientphone(),
                facture.getAmount(),
                facture.getStatus(),
                facture.getShip(),
                facture.getUser(),
                facture.getTarif(),
                facture.getAssurance(),
                facture.getDiscount(),
                facture.getSubtotal(),
                facture.getBalance(),
                facture.getEffectif(),
                facture.getSurcursal(),
                facture.getDestination()
        );
    }

    public static Facture mapToFacture(FactureModel factureModel){
        if(factureModel==null){
            return null;
        }
        return new Facture(
                factureModel.getId(),
                factureModel.getCode(),
                factureModel.getDate(),
                factureModel.getClient(),
                factureModel.getClientphone(),
                factureModel.getAmount(),
                factureModel.getStatus(),
                factureModel.getShip(),
                factureModel.getUser(),
                factureModel.getTarif(),
                factureModel.getAssurance(),
                factureModel.getDiscount(),
                factureModel.getSubtotal(),
                factureModel.getBalance(),
                factureModel.getEffectif(),
                factureModel.getSurcursal(),
                factureModel.getDestination()
        );
    }
}
