package com.velogexpress.mapper;

import com.velogexpress.entity.FactureDetails;
import com.velogexpress.model.FactureDetailsModel;


public class FactureDetailsMapper {
    public static FactureDetailsModel mapToFactureDetailsModel(FactureDetails factureDetails){
        return new FactureDetailsModel(
                factureDetails.getId(),
                FactureMapper.mapToFactureModel(factureDetails.getFacture()),
                factureDetails.getColis(),
                CategoryMapper.mapToCategoryModel(factureDetails.getCategory()),
                factureDetails.getDescription(),
                factureDetails.getFixedprice(),
                factureDetails.getPounds(),
                factureDetails.getFee(),
                factureDetails.getSoubtotal()
        );
    }

    public static FactureDetails mapToFactureDetails(FactureDetailsModel factureDetailsModel){
        return new FactureDetails(
                factureDetailsModel.getId(),
                FactureMapper.mapToFacture(factureDetailsModel.getFacture()),
                factureDetailsModel.getColis(),
                CategoryMapper.mapToCategory(factureDetailsModel.getCategory()),
                factureDetailsModel.getDescription(),
                factureDetailsModel.getFixedprice(),
                factureDetailsModel.getPounds(),
                factureDetailsModel.getFee(),
                factureDetailsModel.getSoubtotal()
        );
    }
}
