package com.velogexpress.mapper;

import com.velogexpress.entity.Amnisty;
import com.velogexpress.model.AmnistyModel;

public class AmnistyMapper {
    public static AmnistyModel mapToAmnistyModel(Amnisty amnisty) {
       return new AmnistyModel(
               amnisty.getId(),
               amnisty.getCategory(),
               amnisty.getPounds(),
               amnisty.getStatus(),
               amnisty.getTracking(),
               amnisty.getPicture(),
               amnisty.getNote(),
               amnisty.getName(),
               amnisty.getTelephone(),
               amnisty.getPrice(),
               amnisty.getDouane(),
               amnisty.getCitypoundfee(),
               amnisty.getUser(),
               amnisty.getCreatedAt()
       );
    }

    public static Amnisty mapToAmnisty(AmnistyModel amnistyModel) {
        return new Amnisty(
                amnistyModel.getId(),
                amnistyModel.getCategory(),
                amnistyModel.getPounds(),
                amnistyModel.getStatus(),
                amnistyModel.getTracking(),
                amnistyModel.getPicture(),
                amnistyModel.getNote(),
                amnistyModel.getName(),
                amnistyModel.getTelephone(),
                amnistyModel.getPrice(),
                amnistyModel.getDouane(),
                amnistyModel.getCitypoundfee(),
                amnistyModel.getUser(),
                amnistyModel.getCreatedAt()
        );
    }
}
