package com.velogexpress.mapper;


import com.velogexpress.entity.TempDetails;
import com.velogexpress.model.TempDetailsModel;

public class TempDetailsMapper {
    public static TempDetailsModel mapToTempDetailsModel(TempDetails tempDetails){
        return new TempDetailsModel(
                tempDetails.getId(),
                tempDetails.getColis(),
                CategoryMapper.mapToCategoryModel(tempDetails.getCategory()),
                tempDetails.getDescription(),
                tempDetails.getFixedprice(),
                tempDetails.getPounds(),
                tempDetails.getFee(),
                tempDetails.getSoubtotal(),
                tempDetails.getClient()
        );
    }

    public static TempDetails mapToTempDetails(TempDetailsModel tempDetailsModel){
        return new TempDetails(
                tempDetailsModel.getId(),
                tempDetailsModel.getColis(),
                CategoryMapper.mapToCategory(tempDetailsModel.getCategory()),
                tempDetailsModel.getDescription(),
                tempDetailsModel.getFixedprice(),
                tempDetailsModel.getPounds(),
                tempDetailsModel.getFee(),
                tempDetailsModel.getSoubtotal(),
                tempDetailsModel.getClient()
        );
    }
}
