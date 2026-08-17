package com.velogexpress.service.impl;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.entity.Mainaddress;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.MainaddressMapper;
import com.velogexpress.model.MainaddressModel;
import com.velogexpress.repository.ClientRegisterRepository;
import com.velogexpress.repository.MainaddressRepository;
import com.velogexpress.service.EmailService;
import com.velogexpress.service.MainaddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainaddressServiceImpl implements MainaddressService {

    private final MainaddressRepository mainaddressRepository;
    private final EmailService emailService;
    private final ClientRegisterRepository clientRegisterRepository;

    @Override
    public MainaddressModel createAddress(MainaddressModel model) {
        Mainaddress entity = MainaddressMapper.mapToMainaddress(model);
        Mainaddress saved = mainaddressRepository.save(entity);
        return MainaddressMapper.mapToMainaddressModel(saved);
    }

    @Override
    public Page<MainaddressModel> getAddress(Pageable pageable) {
        return mainaddressRepository.findAll(pageable)
                .map(MainaddressMapper::mapToMainaddressModel);
    }

    @Override
    public MainaddressModel getAddressById(Long id) {
        Mainaddress entity = mainaddressRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Address does not exist with the given ID: " + id));
        return MainaddressMapper.mapToMainaddressModel(entity);
    }

    @Override
    public MainaddressModel updateAddress(Long id, MainaddressModel model, Pageable pageable) {
        Mainaddress address = mainaddressRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Address does not exist with the given ID: " + id));

        address.setAddressline(model.getAddressline());
        address.setCity(model.getCity());
        address.setState(model.getState());
        address.setZipcode(model.getZipcode());
        address.setPhone(model.getPhone());

        Mainaddress updated = mainaddressRepository.save(address);

        // Notify clients asynchronously
        Page<Clientregister> clients = clientRegisterRepository.findAll(pageable);
        clients.getContent().forEach(client -> {
            String body = "Nous avons une grande nouvelle; on a changé d’adresse!<br>"
                    +" Voici la nouvelle adresse pour placer vos commandes :<br>"
                    +" Adresse : "+address.getAddressline()
                    +" <br>Ville : "+address.getCity()
                    +" <br>État : "+address.getState()
                    +" <br>Code postal : "+address.getZipcode()
                    +" <br>Téléphone : "+client.getPhone();
            emailService.sendMails(client.getEmail(),"Bonjour "+client.getName(),"Changement d’adresse",body);
        });

        return MainaddressMapper.mapToMainaddressModel(updated);
    }
}
