package com.velogexpress.service.impl;

import com.velogexpress.model.CipinfeeModel;
import com.velogexpress.model.TauxModel;
import com.velogexpress.service.CalculatriceService;
import com.velogexpress.service.CipinfeeService;
import com.velogexpress.service.TauxService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class CalculatriceServiceImpl implements CalculatriceService {
    private CipinfeeService cipinfeeService;
    private TauxService tauxService;
    @Override
    public String calculeEstimatePrice(Long city, double poid) {
        TauxModel taux=tauxService.getTauxByDevise("Dollars US");
        CipinfeeModel cipinfee =cipinfeeService.getCipinfeeByCity(city);
        double activeTaux=taux.getSale();
        double activefee=cipinfee.getPounds().getAmount();
        double price=activefee * poid;
        double convert=price*activeTaux;
        double prefix = BigDecimal.valueOf(price)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        double suffix = BigDecimal.valueOf(convert)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        return "$US "+prefix+" ====== $HT "+suffix;
    }
}
