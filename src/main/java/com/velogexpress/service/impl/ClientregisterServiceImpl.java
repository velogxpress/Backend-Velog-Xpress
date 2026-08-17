package com.velogexpress.service.impl;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.entity.Role;
import com.velogexpress.entity.Status;
import com.velogexpress.entity.Ville;
import com.velogexpress.mapper.ClientregisterMapper;
import com.velogexpress.mapper.RegisterMapper;
import com.velogexpress.mapper.VilleMapper;
import com.velogexpress.model.ClientregisterModel;
import com.velogexpress.model.MainaddressModel;
import com.velogexpress.model.RegisterModel;
import com.velogexpress.model.VilleModel;
import com.velogexpress.projection.ClientGraphProjection;
import com.velogexpress.repository.ClientRegisterRepository;
import com.velogexpress.service.ClientregisterService;
import com.velogexpress.service.EmailService;
import com.velogexpress.service.MainaddressService;
import com.velogexpress.service.VilleService;
import com.velogexpress.tools.CreatePIN;
import com.velogexpress.tools.HashPassword;
import com.velogexpress.tools.UserID;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientregisterServiceImpl implements ClientregisterService {
    private ClientRegisterRepository clientRegisterRepository;
    private VilleService villeService;
    private MainaddressService mainaddressService;

    @Autowired private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientregisterModel createUser(ClientregisterModel clientregisterModel) {
        Pageable pageable = PageRequest.of(0,25);
        UserID  uniqueID=new UserID();
        Clientregister clientregister= ClientregisterMapper.mapToclientregister(clientregisterModel);
        VilleModel villeModel=villeService.getVilleByDescription(clientregister.getVille().getDescription());
        Ville ville= VilleMapper.mapToVille(villeModel);
        clientregister.setVille(ville);
        clientregister.setUsercode(uniqueID.SERIAL(ville.getAbreger(), clientregister.getName()));
        clientregister.setPassword(passwordEncoder.encode(clientregisterModel.getPassword()));
        clientregister.setRole(Role.CLIENT.getLabel());
        clientregister.setStatus(Status.ACTIVE.getLabel());
        Page<MainaddressModel> addressList=mainaddressService.getAddress(pageable);
        String corps="On vous simplifie la vie avec un nouveau code d'utilisateur : "+clientregister.getUsercode()+"<br>"
                +" Connectez-vous et profitez de nos services sans souci. En cas de pépin, notre équipe est prête à vous aider!";

        String body="Bienvenue dans l'équipe de Velog Express.<br>"
                +" Note: Tous les sites e-commerce n'acceptent pas la même configuration d'adresse."
                +" Certains acceptent le code unique alphanumérique à côté de votre nom, tandis que d'autres ne l'acceptent pas."
                +" Dans ce cas, voici les deux façons dont vous pouvez configurer votre adresse:<br><br>"
                +" Première façon: <br>"
                +" Avec le code("+clientregister.getUsercode()+") a coté de votre nom<br>"
                +" Full Name: "+clientregisterModel.getName()+" "+clientregister.getUsercode()+"<br>"
                +" Address line 1: "+addressList.getContent().get(0).getAddressline()+"<br>"
                +" Address line 2: <br>"
                +" City: "+addressList.getContent().get(0).getCity()+"<br>"
                +" State: "+addressList.getContent().get(0).getState()+"<br>"
                +" Zip Code: "+addressList.getContent().get(0).getZipcode()+"<br>"
                +" Phone: "+addressList.getContent().get(0).getPhone()+"<br><br>"
                +" Deuxième façon: <br>"
                +" Avec le code("+clientregister.getUsercode()+") dans l'adresse Ligne 2<br>"
                +" Full Name: "+clientregisterModel.getName()+"<br>"
                +" Address line 1: "+addressList.getContent().get(0).getAddressline()+"<br>"
                +" Address line 2: "+clientregister.getUsercode()+"<br>"
                +" City: "+addressList.getContent().get(0).getCity()+"<br>"
                +" State: "+addressList.getContent().get(0).getState()+"<br>"
                +" Zip Code: "+addressList.getContent().get(0).getZipcode()+"<br>"
                +" Phone: "+addressList.getContent().get(0).getPhone();
        Clientregister saveuser=clientRegisterRepository.save(clientregister);

        emailService.sendMails(clientregisterModel.getEmail(), "Salut "+clientregisterModel.getName(),"Bienvenue",corps);
        emailService.sendMails(clientregisterModel.getEmail(),"Chèr(e) "+clientregisterModel.getName(),"Configuration d'adresse",body);
        return ClientregisterMapper.mapToclientregisterModel(saveuser);
    }

    @Override
    public ClientregisterModel createUtilisateur(ClientregisterModel clientregisterModel) {
        Pageable pageable = PageRequest.of(0,25);
        UserID  uniqueID=new UserID();
        String contrasena= CreatePIN.generatePassword();
        Clientregister clientregister= ClientregisterMapper.mapToclientregister(clientregisterModel);
        VilleModel villeModel=villeService.getVilleByDescription(clientregister.getVille().getDescription());
        Ville ville= VilleMapper.mapToVille(villeModel);
        clientregister.setVille(ville);
        clientregister.setUsercode(uniqueID.SERIAL(ville.getAbreger(), clientregister.getName()));
        clientregister.setPassword(passwordEncoder.encode(contrasena));
        clientregister.setRole(Role.CLIENT.getLabel());
        clientregister.setStatus(Status.ACTIVE.getLabel());
        Page<MainaddressModel> addressList=mainaddressService.getAddress(pageable);
        String corps="On vous simplifie la vie avec un nouveau code d'utilisateur : "+clientregister.getUsercode()+"<br>"
                +" Connectez-vous et profitez de nos services sans souci. En cas de pépin, notre équipe est prête à vous aider!";

        String cuerpo="Votre compte a été créé avec succès dans notre système.<br>" +
                "Voici vos informations de connexion :<br>" +
                "Email : " +clientregisterModel.getEmail()+"<br>" +
                "Mot de passe initial : " +contrasena+"<br>" +
                "Nous vous recommandons de vous connecter dès maintenant et de modifier ce mot de passe pour garantir la sécurité de votre compte.<br>" +
                "Si vous n’êtes pas à l’origine de la création de ce compte, veuillez contacter notre service d’assistance immédiatement.<br>" +
                "Support : info@velogxpress.com";

        String body="Bienvenue dans l'équipe de Velog Express.<br>"
                +" Note: Tous les sites e-commerce n'acceptent pas la même configuration d'adresse."
                +" Certains acceptent le code unique alphanumérique à côté de votre nom, tandis que d'autres ne l'acceptent pas."
                +" Dans ce cas, voici les deux façons dont vous pouvez configurer votre adresse:<br><br>"
                +" Première façon: <br>"
                +" Avec le code("+clientregister.getUsercode()+") a coté de votre nom<br>"
                +" Full Name: "+clientregisterModel.getName()+" "+clientregister.getUsercode()+"<br>"
                +" Address line 1: "+addressList.getContent().get(0).getAddressline()+"<br>"
                +" Address line 2: <br>"
                +" City: "+addressList.getContent().get(0).getCity()+"<br>"
                +" State: "+addressList.getContent().get(0).getState()+"<br>"
                +" Zip Code: "+addressList.getContent().get(0).getZipcode()+"<br>"
                +" Phone: "+addressList.getContent().get(0).getPhone()+"<br><br>"
                +" Deuxième façon: <br>"
                +" Avec le code("+clientregister.getUsercode()+") dans l'adresse Ligne 2<br>"
                +" Full Name: "+clientregisterModel.getName()+"<br>"
                +" Address line 1: "+addressList.getContent().get(0).getAddressline()+"<br>"
                +" Address line 2: "+clientregister.getUsercode()+"<br>"
                +" City: "+addressList.getContent().get(0).getCity()+"<br>"
                +" State: "+addressList.getContent().get(0).getState()+"<br>"
                +" Zip Code: "+addressList.getContent().get(0).getZipcode()+"<br>"
                +" Phone: "+addressList.getContent().get(0).getPhone();
        Clientregister saveuser=clientRegisterRepository.save(clientregister);

        emailService.sendMails(clientregisterModel.getEmail(), "Salut "+clientregisterModel.getName(),"Bienvenue",corps);
        emailService.sendMails(clientregisterModel.getEmail(),"Chèr(e) "+clientregisterModel.getName(),"Configuration d'adresse",body);
        emailService.sendMails(clientregisterModel.getEmail(),"Bonjour "+clientregisterModel.getName(),"Création de compte",cuerpo);
        return ClientregisterMapper.mapToclientregisterModel(saveuser);
    }

    @Override
    public Page<ClientregisterModel> getAllClienteregister(Pageable pageable) {
       return clientRegisterRepository.findAll(pageable)
               .map(ClientregisterMapper::mapToclientregisterModel);
    }

    @Override
    public Page<ClientregisterModel> getAllAgent(Pageable pageable) {
        return clientRegisterRepository.findByRole("Agent",pageable)
                .map(ClientregisterMapper::mapToclientregisterModel);
    }

    @Override
    public Page<ClientregisterModel> getAgent(String param, Pageable pageable) {
        return clientRegisterRepository.search(param, pageable)
                .map(ClientregisterMapper::mapToclientregisterModel);
    }

    @Override
    public ClientregisterModel getClientregisterByUsercode(String code) {
        Clientregister clientregister=clientRegisterRepository.findByUserCodeOrEmail(code);
        if(clientregister!=null){
            return ClientregisterMapper.mapToclientregisterModel(clientregister);
        }else{
            return null;
        }
    }

    @Override
    public RegisterModel getRegisterByUsercode(String code) {
        Clientregister clientregister=clientRegisterRepository.findByUserCodeOrEmail(code);
        if(clientregister!=null){
            return RegisterMapper.mapToRegisterModel(clientregister);
        }else{
            return null;
        }
    }

    @Override
    public ClientregisterModel updateClientregister(String code, ClientregisterModel clientregisterModel) {
        Clientregister clientregister=clientRegisterRepository.findByUserCodeOrEmail(code);
        if(clientregister!=null){
            clientregister.setName(clientregisterModel.getName());
            clientregister.setEmail(clientregisterModel.getEmail());
            clientregister.setRole(clientregisterModel.getRole());
            clientregister.setPhone(clientregisterModel.getPhone());
            clientregister.setStatus(clientregisterModel.getStatus());
            clientregister.setAddress(clientregisterModel.getAddress());
            VilleModel villeModel=villeService.getVilleByDescription(clientregisterModel.getVille().getDescription());
            Ville ville= VilleMapper.mapToVille(villeModel);
            clientregister.setVille(ville);

            Clientregister saveobj=clientRegisterRepository.save(clientregister);
            return ClientregisterMapper.mapToclientregisterModel(saveobj);
        }else{
            return null;
        }

    }

    @Override
    public ClientregisterModel updatePassword(String code, ClientregisterModel clientregisterModel) {
        Clientregister clientregister=clientRegisterRepository.findByUserCodeOrEmail(code);
        if(clientregister!=null){
            clientregister.setPassword(passwordEncoder.encode(clientregisterModel.getPassword()));
            Clientregister saveobj=clientRegisterRepository.save(clientregister);
            String body="Le mot de passe de votre compte a été modifié avec succès. Si vous n’avez pas initié ce " +
                    "changement, veuillez nous contacter immédiatement sur info@velogxpress.com";

            emailService.sendMails(clientregister.getEmail(),"Bonjour "+clientregisterModel.getName(),"Changement de mot de passe",body);
            return ClientregisterMapper.mapToclientregisterModel(saveobj);

        }else{
            return null;
        }

    }

    @Override
    public Long getCountUser(String user, Pageable pageable) {
        return clientRegisterRepository.count();

    }

    @Override
    public List<ClientregisterModel> getCountGraphe() {
        List<Clientregister> client=clientRegisterRepository.findClientandCity();
        return client.stream().map(ClientregisterMapper::mapToclientregisterModel).collect(Collectors.toList());
    }

    @Override
    public Long countClient() {
        return clientRegisterRepository.count();
    }

    @Override
    public ClientregisterModel EditUserInfo(String id, ClientregisterModel clientregisterModel) {
        Clientregister clientregister=clientRegisterRepository.findByUserCodeOrEmail(id);
        if(clientregister==null){
            return null;
        }
        clientregister.setName(clientregisterModel.getName());
        clientregister.setEmail(clientregisterModel.getEmail());
        clientregister.setPhone(clientregisterModel.getPhone());
        clientregister.setAddress(clientregisterModel.getAddress());
        Clientregister saveobj=clientRegisterRepository.save(clientregister);
        return ClientregisterMapper.mapToclientregisterModel(saveobj);
    }

    @Override
    public void deleteUser(String id) {
        Clientregister clientregister=clientRegisterRepository.findByUserCodeOrEmail(id);
        if(clientregister!=null){
            clientRegisterRepository.delete(clientregister);
        }
    }

    @Override
    public String findExistEmail(String email) {
        Clientregister clientregister=clientRegisterRepository.findByUserCodeOrEmail(email);
        if(clientregister!=null){
            return "Exists";
        }
        return "Not Exists";
    }


}
