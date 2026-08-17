package com.velogexpress.service.impl;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.entity.Recovery;
import com.velogexpress.entity.Status;
import com.velogexpress.exception.RessourceNotFoundException;
import com.velogexpress.mapper.RecoveryMapper;
import com.velogexpress.model.RecoveryModel;
import com.velogexpress.repository.ClientRegisterRepository;
import com.velogexpress.repository.RecoveryRepository;
import com.velogexpress.service.EmailService;
import com.velogexpress.service.RecoveryService;
import com.velogexpress.tools.CreatePIN;
import com.velogexpress.tools.DateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecoveryServiceImpl implements RecoveryService {

    private final RecoveryRepository recoveryRepository;
    private final EmailService emailService;
    private final ClientRegisterRepository clientRegisterRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public RecoveryModel createPIN(RecoveryModel recoveryModel) {
        // Map DTO to entity
        Recovery recovery = RecoveryMapper.mapToRecovery(recoveryModel);

        // Set generated fields
        recovery.setDate(DateTime.CURRENTDATE());
        recovery.setCode(CreatePIN.GENERATEPIN());
        recovery.setEmail(recoveryModel.getEmail());
        recovery.setStatus(Status.NOVALIDATE.getLabel());

        // Save to DB
        Recovery saved = recoveryRepository.save(recovery);

        // Send email
        String encodedEmail = URLEncoder.encode(recoveryModel.getEmail(), StandardCharsets.UTF_8);
        String verificationLink = "https://www.velogxpress.com/dashboard/verifyotp?email=" + encodedEmail;
        String body = "Votre PIN de récupération de mot de passe est: <br><h1><strong>" + recovery.getCode()+ "</strong></h1><br>"
                + "Cliquez sur le bouton ci-dessous pour valider ce même code et choisir un nouveau mot de passe.<br><br>"
                + "<a href='" + verificationLink + "' style='display:inline-block;padding:12px 20px;background:#001B90;color:#ffffff;text-decoration:none;border-radius:8px;font-weight:bold;'>Valider mon code OTP</a><br><br>"
                + "Si le bouton ne s’ouvre pas, copiez ce lien dans votre navigateur:<br>"
                + "<a href='" + verificationLink + "'>" + verificationLink + "</a><br><br>"
                + "Si vous n’êtes pas à l’origine de cette demande, vous pouvez ignorer ce message en toute sécurité.";
        emailService.sendMails(recoveryModel.getEmail(),"Chèr(e) utilisateur ","OTP",body);
        return RecoveryMapper.mapToRecoveryModel(saved);
    }

    @Override
    public Page<RecoveryModel> getAllPIN(Pageable pageable) {
        return recoveryRepository.findAll(pageable)
                .map(RecoveryMapper::mapToRecoveryModel);
    }

    @Override
    public Optional<RecoveryModel> getPINByUser(String email) {
        return recoveryRepository.findPendingByEmail(email)
                .map(RecoveryMapper::mapToRecoveryModel);
    }

    @Override
    public RecoveryModel updatePINByUser(String email) {
        Long id = recoveryRepository.findMaxIDRecovery(email);

        if (id == null) {
            throw new RessourceNotFoundException("No pending recovery found for user: " + email);
        }

        Recovery recovery = recoveryRepository.findPingByEmail(id);

        // ✅ UPDATE STATUS
        recovery.setStatus(Status.VALIDATE.getLabel());

        // 🔥 GENERATE TOKEN
        String token = UUID.randomUUID().toString();

        recovery.setResetToken(token);
        recovery.setTokenExpiration(LocalDateTime.now().plusMinutes(30));

        Recovery saved = recoveryRepository.save(recovery);

        // 🔗 FRONTEND LINK
        //String resetLink = "http://localhost:3000/dashboard/reset-password?token=" + token;
        String resetLink = "https://www.velogxpress.com/dashboard/reset-password?token=" + token;

        // 📧 EMAIL BODY
        String body =
                "Votre code OTP a été validé avec succès.<br><br>" +
                        "Pour réinitialiser votre mot de passe, utilisez le lien sécurisé ci-dessous :<br>" +
                        "<a href='" + resetLink + "'>Réinitialiser mon mot de passe</a><br><br>" +
                        "⚠️ Ce lien expirera automatiquement après 15 minutes.";

        emailService.sendMails(email, "Réinitialisation mot de passe", "RESET PASSWORD", body);

        return RecoveryMapper.mapToRecoveryModel(saved);
    }

    @Override
    public RecoveryModel getRecovery(String email) {
        Long id=recoveryRepository.findMaxIDRecovery(email);
        if(id==null) {
            throw new RessourceNotFoundException("No pending recovery found for user: " + email);
        }else{
            Recovery recovery=recoveryRepository.findPingByEmail(id);
            return RecoveryMapper.mapToRecoveryModel(recovery);
        }

    }

    @Override
    public String verifyToken(String token) {

            Recovery recovery = recoveryRepository.findByResetToken(token);

            if (recovery == null) {
                return "Token invalide";
            }

            if (recovery.getTokenExpiration().isBefore(LocalDateTime.now())) {
                return"Token expiré";
            }

            return "Token valide";

    }

    @Override
    public String resetPassword(String token, String newPassword) {
        Recovery recovery = recoveryRepository.findByResetToken(token);

        if (recovery == null) {
            return "Token invalide";
        }

        if (recovery.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return "Token expiré";
        }

        // 🔐 Update password user
        Clientregister user = clientRegisterRepository.findByUserCodeOrEmail(recovery.getEmail());
        if (user == null) {
            return "Utilisateur introuvable";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        clientRegisterRepository.saveAndFlush(user);

        // ❌ invalidate token
        recovery.setResetToken(null);
        recoveryRepository.saveAndFlush(recovery);

        return "Mot de passe mis à jour";
    }
}
