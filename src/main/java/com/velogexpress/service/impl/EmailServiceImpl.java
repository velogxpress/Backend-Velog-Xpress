package com.velogexpress.service.impl;

import com.velogexpress.entity.EmailDetails;
import com.velogexpress.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {
    private static final String PUBLIC_PRODUCTS_URL =
            "https://www.velogxpress.com/api/uploads/products/";
//    @Autowired private JavaMailSender javaMailSender;
//    @Value("${spring.mail.username}") private String sender;

    @Autowired
    @Qualifier("sendGridMailSender")
    private JavaMailSender sendGridMailSender;

    @Autowired
    @Qualifier("hostingerMailSender")
    private JavaMailSender hostingerMailSender;


    @Override
    public String sendMails(String recipient, String name, String subject, String body) {
        try {
            // Charger le template HTML
            ClassPathResource resource =
                    new ClassPathResource("templates/email-template.html");

            String htmlTemplate = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // Remplacer les variables dans le HTML
            String htmlBody = htmlTemplate
                    .replace("{{name}}", name)
                    .replace("{{body}}",body);

            // Créer le message
           // MimeMessage message = hostingerMailSender.createMimeMessage();
            MimeMessage message = sendGridMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@velogxpress.com");
            helper.setReplyTo("info@velogxpress.com");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // 'true' pour activer HTML


            // Envoyer le message
           // hostingerMailSender.send(message);
            sendGridMailSender.send(message);
            return "Email envoyé avec succès !";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Erreur lors de l’envoi du mail.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Problème lors du chargement du template.";
        }
    }

    @Override
    public String sendEmailToContact(String to,String name, String subject, String body) {
        try {
            // Charger le template HTML
            ClassPathResource resource =
                    new ClassPathResource("templates/email-template.html");

            String htmlTemplate = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // Remplacer les variables dans le HTML
            String htmlBody = htmlTemplate
                    .replace("{{name}}", name)
                    .replace("{{body}}",body);

            // Créer le message
            //MimeMessage message = hostingerMailSender.createMimeMessage();
            MimeMessage message = hostingerMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("info@velogxpress.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // 'true' pour activer HTML


            // Envoyer le message
            //hostingerMailSender.send(message);
            sendGridMailSender.send(message);
            return "Email envoyé avec succès !";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Erreur lors de l’envoi du mail.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Problème lors du chargement du template.";
        }
    }

    @Override
    public String sendMailWithAttachments(String recipient, String name, String subject, String body, List<String> files) {
        try {
            // Charger le template HTML
            ClassPathResource resource =
                    new ClassPathResource("templates/files-template.html");

            String htmlTemplate = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // Construire la liste des fichiers en <li>
            String attachmentsHtml = files.stream()
                    .map(path -> new File(path).getName())
                    .map(fileName -> "<li>" + fileName + "</li>")
                    .collect(Collectors.joining());

            // Remplacer les variables dans le HTML
            String htmlBody = htmlTemplate
                    .replace("{{name}}", name)
                    .replace("{{body}}",body)
                    .replace("{{attachments}}", attachmentsHtml);

            // Créer le message
           // MimeMessage message = hostingerMailSender.createMimeMessage();
            MimeMessage message = sendGridMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@velogxpress.com");
            helper.setReplyTo("info@velogxpress.com");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // 'true' pour activer HTML

            // Ajouter les pièces jointes
            for (String path : files) {
                FileSystemResource file = new FileSystemResource(new File(path));
                if (file.exists()) {
                    helper.addAttachment(file.getFilename(), file);
                }
            }

            // Envoyer le message
            //hostingerMailSender.send(message);
            sendGridMailSender.send(message);
            return "Email envoyé avec succès !";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Erreur lors de l’envoi du mail.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Problème lors du chargement du template.";
        }
    }

    @Override
    public String sendMailWithDownloadLinks(String recipient, String name, String subject, String body, List<String> files) {
        try {
            ClassPathResource resource =
                    new ClassPathResource("templates/files-template.html");

            String htmlTemplate = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // Construire les boutons download
            String attachmentsHtml = files.stream()
                    .map(link -> {
                        String lowerLink = link.toLowerCase();
                        String downloadUrl = PUBLIC_PRODUCTS_URL
                                + UriUtils.encodePathSegment(link, StandardCharsets.UTF_8);
                        String safeDownloadUrl = HtmlUtils.htmlEscape(downloadUrl);

                        String buttonText;

                        if (lowerLink.endsWith(".pdf")) {
                            buttonText = "📄 Télécharger la facture";
                        } else if (
                                lowerLink.endsWith(".jpg") ||
                                        lowerLink.endsWith(".jpeg") ||
                                        lowerLink.endsWith(".png") ||
                                        lowerLink.endsWith(".webp")
                        ) {
                            buttonText = "🖼️ Télécharger l’image du colis";
                        } else {
                            buttonText = "📥 Télécharger le fichier";
                        }

                        return """
                <p style="margin:10px 0;">
                    <a href="%s"
                       target="_blank"
                       style="
                            background:#0e2269;
                            color:#ffffff;
                            padding:10px 18px;
                            text-decoration:none;
                            border-radius:6px;
                            display:inline-block;
                            font-weight:bold;">
                        %s
                    </a>
                    <br>
                    <a href="%s"
                       target="_blank"
                       style="display:inline-block; margin-top:8px; color:#0e2269;
                              font-size:12px; overflow-wrap:anywhere; word-break:break-all;">
                        %s
                    </a>
                </p>
            """.formatted(safeDownloadUrl, buttonText, safeDownloadUrl, safeDownloadUrl);
                    })
                    .collect(Collectors.joining());

            String htmlBody = htmlTemplate
                    .replace("{{name}}", name)
                    .replace("{{body}}", body)
                    .replace("{{attachments}}", attachmentsHtml);

            MimeMessage message =
                    sendGridMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@velogxpress.com");
            helper.setReplyTo("info@velogxpress.com");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            sendGridMailSender.send(message);

            return "Email envoyé avec succès !";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l’envoi du mail.";
        }
    }
}
