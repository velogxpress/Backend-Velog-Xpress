package com.velogexpress.service;
import com.velogexpress.entity.EmailDetails;

import java.util.List;

public interface EmailService {
    String sendMails(String recipient,String name,String subject,String body);
    String sendEmailToContact(String to,String name, String subject, String body);
    String sendMailWithAttachments(String recipient, String name, String subject,String body, List<String> files);
    String sendMailWithDownloadLinks(String recipient, String name, String subject,String body, List<String> files);
}
