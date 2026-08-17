package com.velogexpress.service.impl;

import com.velogexpress.entity.Historique;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.HistoriqueMapper;
import com.velogexpress.model.HistoriqueModel;
import com.velogexpress.repository.HistoriqueRepository;
import com.velogexpress.service.HistoriqueService;
import com.velogexpress.tools.DateTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HistoriqueServiceImpl implements HistoriqueService {

    private final HistoriqueRepository historiqueRepository;

    @Override
    public HistoriqueModel createHistorique(HistoriqueModel model) {
        Historique historique = HistoriqueMapper.mapToHistorique(model);
        historique.setLogin(DateTime.CURRENTDATETIME());
        historique.setLogout("N/A");
        return HistoriqueMapper.mapToHistoriqueModel(historiqueRepository.save(historique));
    }

    @Override
    public Page<HistoriqueModel> getAllHistorique(Pageable pageable) {
        return historiqueRepository.getAllHistoriqueLoginList(pageable)
                .map(HistoriqueMapper::mapToHistoriqueModel);
    }

    @Override
    public Page<HistoriqueModel> getAllHistoriqueByUser(String user, Pageable pageable) {
        return historiqueRepository.getHistoriqueLoginList(user, pageable)
                .map(HistoriqueMapper::mapToHistoriqueModel);
    }

    @Override
    public HistoriqueModel getLatestHistoriqueByUser(String user) {
        Historique historique = historiqueRepository.getHistoriqueLogin(user);
        if (historique == null) {
            throw new RessourceNotFoundException("No login found for user: " + user);
        }
        return HistoriqueMapper.mapToHistoriqueModel(historique);
    }

    @Override
    public HistoriqueModel updateLogoutTime(String user) {
        Historique historique = historiqueRepository.getHistoriqueLogin(user);
        if (historique == null) {
            throw new RessourceNotFoundException("No active login found for user: " + user);
        }
        historique.setLogout(DateTime.CURRENTDATETIME());
        return HistoriqueMapper.mapToHistoriqueModel(historiqueRepository.save(historique));
    }
}
