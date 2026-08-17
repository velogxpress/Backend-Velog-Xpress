package com.velogexpress.service;

public interface NotificationService {
    void sendPushNotification(String userEmail, String title, String body);
}
