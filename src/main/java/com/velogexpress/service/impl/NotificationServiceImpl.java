package com.velogexpress.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velogexpress.service.NotificationService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String NOTIF_SERVER_URL = "https://velog-notif-production.up.railway.app/notifications/trigger";
    @Value("${notification.api-key}")
    private String API_KEY;

    @Async // Très important pour ne pas bloquer la requête utilisateur !
    public void sendPushNotification(String userEmail, String title, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", API_KEY);

            // Construction du JSON via Jackson (échappe correctement guillemets, retours à la ligne, etc.)
            Map<String, String> payload = new HashMap<>();
            payload.put("userEmail", userEmail);
            payload.put("title", title);
            payload.put("body", body);
            String requestJson = objectMapper.writeValueAsString(payload);

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            restTemplate.postForObject(NOTIF_SERVER_URL, entity, String.class);
        } catch (Exception e) {
            // Loggez l'erreur, mais ne crashez pas le processus principal !
            System.err.println("Erreur lors de l'envoi de la notification: " + e.getMessage());
        }
    }
}