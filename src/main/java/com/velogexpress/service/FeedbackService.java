package com.velogexpress.service;

import com.velogexpress.model.FeedbackModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {
    FeedbackModel createFeedback(FeedbackModel feedbackModel);
    Page<FeedbackModel> getAllFeedback(Pageable pageable);
    Page<FeedbackModel> getFeedbackByEmail(String email, Pageable pageable);
    Page<FeedbackModel> getFeedbackByUser(String email, Pageable pageable);
    FeedbackModel updateFeedback(Long ID);
    Page<FeedbackModel> getUnreadFeedback(Pageable pageable);
    Page<FeedbackModel> getReadFeedback(Pageable pageable);
    void replyFeedback(String recipient, String body,String subject);
    Long countFeedback();
}
