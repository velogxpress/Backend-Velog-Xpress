package com.velogexpress.mapper;

import com.velogexpress.entity.Recovery;
import com.velogexpress.model.RecoveryModel;

public class RecoveryMapper {
    public static RecoveryModel mapToRecoveryModel(Recovery recovery){
        return new RecoveryModel(
                recovery.getId(),
                recovery.getEmail(),
                recovery.getDate(),
                recovery.getCode(),
                recovery.getStatus(),
                recovery.getResetToken(),
                recovery.getTokenExpiration()
        );
    }

    public static Recovery mapToRecovery(RecoveryModel recoveryModel){
        return new Recovery(
                recoveryModel.getId(),
                recoveryModel.getEmail(),
                recoveryModel.getDate(),
                recoveryModel.getCode(),
                recoveryModel.getStatus(),
                recoveryModel.getResetToken(),
                recoveryModel.getTokenExpiration()
        );
    }
}
