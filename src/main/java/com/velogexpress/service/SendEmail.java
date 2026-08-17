package com.velogexpress.service;

import com.velogexpress.model.StorageModel;

public interface SendEmail {
    public String sendEmail(String to, String subject, String body);
    public String sendEmail(String subject, String body);
}
