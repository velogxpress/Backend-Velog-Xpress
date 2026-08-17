package com.velogexpress.service.impl;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.repository.ClientRegisterRepository;
import com.velogexpress.service.EmailService;
import com.velogexpress.service.SendEmail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SendEmailServiceImpl implements SendEmail {
    @Autowired
    EmailService emailService;
    private final ClientRegisterRepository clientRegisterRepository;
    @Override
    public String sendEmail(String to, String subject, String body) {
        String corps = "<p style=\"text-align: justify;\">" + body + "</p>";
        emailService.sendEmailToContact(to, "Cher(e) client(e)", subject, corps);
        return "Success";
    }

    @Override
    public String sendEmail(String subject, String body) {
        List<Clientregister> clientregisters = clientRegisterRepository.findAll();
        String corps = "<p style=\"text-align: justify;\">" + body + "</p>";
        if(clientregisters.isEmpty()){
            return "Unsuccessful";
        }else{
            for(Clientregister clientregister : clientregisters){
                emailService.sendEmailToContact(clientregister.getEmail(), "Cher(e) client(e)", subject, corps);
            }
            return "Success";
        }
    }
}
