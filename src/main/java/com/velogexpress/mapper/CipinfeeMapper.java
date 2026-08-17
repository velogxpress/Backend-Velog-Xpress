package com.velogexpress.mapper;

import com.velogexpress.entity.Cipinfee;
import com.velogexpress.model.CipinfeeModel;

public class CipinfeeMapper {
    public static CipinfeeModel mapToCipinfeeModel(Cipinfee cipinfee){
        return new CipinfeeModel(
                cipinfee.getId(),
                VilleMapper.mapToVilleModel(cipinfee.getCity()),
                FeepoundsMapper.mapToFeepoundsModel(cipinfee.getPounds()),
                InsuranceMapper.mapToInssuranceModel(cipinfee.getInsurance()),
                SpecialfeeMapper.mapToSpecialfeeModel(cipinfee.getSpecialfee())
        );
    }

    public static Cipinfee mapToCipinfee(CipinfeeModel cipinfeeModel){
        return new Cipinfee(
                cipinfeeModel.getId(),
                VilleMapper.mapToVille(cipinfeeModel.getCity()),
                FeepoundsMapper.mapToFeepounds(cipinfeeModel.getPounds()),
                InsuranceMapper.mapToInssurance(cipinfeeModel.getInsurance()),
                SpecialfeeMapper.mapToSpecialfee(cipinfeeModel.getSpecialfee())
        );
    }
}
