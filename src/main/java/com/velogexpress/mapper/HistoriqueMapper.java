package com.velogexpress.mapper;

import com.velogexpress.entity.Historique;
import com.velogexpress.model.HistoriqueModel;

public class HistoriqueMapper {
    public static HistoriqueModel mapToHistoriqueModel(Historique historique){
        return new HistoriqueModel(
                historique.getId(),
                ClientregisterMapper.mapToclientregisterModel(historique.getUser()),
                historique.getLogin(),
                historique.getPlace(),
                historique.getLogout()
        );
    }

    public static Historique mapToHistorique(HistoriqueModel historiqueModel){
        return new Historique(
                historiqueModel.getId(),
                ClientregisterMapper.mapToclientregister(historiqueModel.getUser()),
                historiqueModel.getLogin(),
                historiqueModel.getPlace(),
                historiqueModel.getLogout()
        );
    }
}
