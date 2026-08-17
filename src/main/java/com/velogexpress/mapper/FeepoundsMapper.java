package com.velogexpress.mapper;

import com.velogexpress.entity.Feepounds;
import com.velogexpress.model.FeepoundsModel;

public class FeepoundsMapper {
    public static FeepoundsModel mapToFeepoundsModel(Feepounds feepounds){
        return new FeepoundsModel(
                feepounds.getId(),
                feepounds.getAmount()
        );
    }

    public static Feepounds mapToFeepounds(FeepoundsModel feepoundsModel){
        return new Feepounds(
                feepoundsModel.getId(),
                feepoundsModel.getAmount()
        );
    }
}
