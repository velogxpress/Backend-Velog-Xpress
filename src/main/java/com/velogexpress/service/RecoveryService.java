package com.velogexpress.service;

import com.velogexpress.model.RecoveryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RecoveryService {
    RecoveryModel createPIN(RecoveryModel recoveryModel);
    Page<RecoveryModel> getAllPIN(Pageable pageable);
    Optional<RecoveryModel> getPINByUser(String user);
    RecoveryModel updatePINByUser(String user);
    RecoveryModel getRecovery(String email);
    String verifyToken(String token);
    String resetPassword(String token,String newPassword);
}
