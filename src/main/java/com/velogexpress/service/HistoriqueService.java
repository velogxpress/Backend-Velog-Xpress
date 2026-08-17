package com.velogexpress.service;

import com.velogexpress.model.HistoriqueModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface HistoriqueService {
    HistoriqueModel createHistorique(HistoriqueModel historiqueModel);
    Page<HistoriqueModel> getAllHistorique(Pageable pageable);
    Page<HistoriqueModel> getAllHistoriqueByUser(String user, Pageable pageable);
    HistoriqueModel getLatestHistoriqueByUser(String user);
    HistoriqueModel updateLogoutTime(String user);
}
