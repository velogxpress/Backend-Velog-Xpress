package com.velogexpress.mapper;

import com.velogexpress.entity.Insurance;
import com.velogexpress.model.InsuranceModel;

public class InsuranceMapper{
    public static InsuranceModel mapToInssuranceModel(Insurance insurance){
        return new InsuranceModel(
                insurance.getId(),
                insurance.getAmount()
        );
    }

    public static Insurance mapToInssurance(InsuranceModel inssuranceModel){
        return new Insurance(
                inssuranceModel.getId(),
                inssuranceModel.getAmount()
        );
    }
}
