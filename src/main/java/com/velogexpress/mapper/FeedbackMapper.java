package com.velogexpress.mapper;

import com.velogexpress.entity.Feedback;
import com.velogexpress.model.FeedbackModel;

public class FeedbackMapper {
    public static FeedbackModel mapToFeedbackModel(Feedback feedback){
        return new FeedbackModel(
                feedback.getId(),
                feedback.getName(),
                feedback.getEmail(),
                feedback.getPhone(),
                feedback.getSubject(),
                feedback.getMessage(),
                feedback.getStatus(),
                feedback.getDate()
        );
    }
    public static Feedback mapToFeedback(FeedbackModel feedbackModel){
        return new Feedback(
                feedbackModel.getId(),
                feedbackModel.getName(),
                feedbackModel.getEmail(),
                feedbackModel.getPhone(),
                feedbackModel.getSubject(),
                feedbackModel.getMessage(),
                feedbackModel.getStatus(),
                feedbackModel.getDate()
        );
    }

}
