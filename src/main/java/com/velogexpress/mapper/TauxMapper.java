package com.velogexpress.mapper;


import com.velogexpress.entity.Taux;
import com.velogexpress.model.TauxModel;

public class TauxMapper {
    public static TauxModel mapToTauxModel(Taux taux){
        return new TauxModel(
                taux.getId(),
                taux.getDevise(),
                taux.getBuy(),
                taux.getSale(),
                taux.getSymbole()
        );
    }

    public static Taux mapToTaux(TauxModel tauxModel){
        return new Taux(
                tauxModel.getId(),
                tauxModel.getDevise(),
                tauxModel.getBuy(),
                tauxModel.getSale(),
                tauxModel.getSymbole()
        );
    }
}
