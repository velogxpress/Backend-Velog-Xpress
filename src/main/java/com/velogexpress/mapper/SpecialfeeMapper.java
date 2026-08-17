package com.velogexpress.mapper;

import com.velogexpress.entity.Specialfee;
import com.velogexpress.model.SpecialfeeModel;

public class SpecialfeeMapper {
    public static SpecialfeeModel mapToSpecialfeeModel(Specialfee specialfee){
        return new SpecialfeeModel(
                specialfee.getId(),
                specialfee.getAmount()
        );
    }

    public static Specialfee mapToSpecialfee(SpecialfeeModel specialfeeModel){
        return new Specialfee(
                specialfeeModel.getId(),
                specialfeeModel.getAmount()
        );
    }
}
