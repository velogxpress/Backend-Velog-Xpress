package com.velogexpress.service.impl;

import com.velogexpress.entity.Feedback;
import com.velogexpress.mapper.FeedbackMapper;
import com.velogexpress.model.FeedbackModel;
import com.velogexpress.repository.FeedbackRepository;
import com.velogexpress.service.EmailService;
import com.velogexpress.service.FeedbackService;
import com.velogexpress.tools.DateTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmailService emailService;

    private static final String STATUS_UNREAD = "U/R";
    private static final String STATUS_READ = "A/R";

    @Override
    public FeedbackModel createFeedback(FeedbackModel feedbackModel) {
        DateTime dt = new DateTime();
        Feedback feedback = FeedbackMapper.mapToFeedback(feedbackModel);
        feedback.setStatus(STATUS_UNREAD);
        feedback.setDate(dt.CURRENTDATETIME());
        Feedback saved = feedbackRepository.save(feedback);
        return FeedbackMapper.mapToFeedbackModel(saved);
    }

    @Override
    public Page<FeedbackModel> getAllFeedback(Pageable pageable) {
        return feedbackRepository.findAll(pageable)
                .map(FeedbackMapper::mapToFeedbackModel);
    }

    @Override
    public Page<FeedbackModel> getFeedbackByEmail(String email, Pageable pageable) {
        return feedbackRepository.findByEmailOrderByIdDesc(email, pageable)
                .map(FeedbackMapper::mapToFeedbackModel);
    }

    @Override
    public Page<FeedbackModel> getFeedbackByUser(String email, Pageable pageable) {
        return getFeedbackByEmail(email, pageable);
    }

    @Override
    public FeedbackModel updateFeedback(Long id) {
        return feedbackRepository.findById(id)
                .map(fb -> {
                    fb.setStatus(STATUS_READ);
                    return FeedbackMapper.mapToFeedbackModel(feedbackRepository.save(fb));
                })
                .orElse(null);
    }

    @Override
    public Page<FeedbackModel> getUnreadFeedback(Pageable pageable) {
        return feedbackRepository.findAllUnread(pageable)
                .map(FeedbackMapper::mapToFeedbackModel);
    }

    @Override
    public Page<FeedbackModel> getReadFeedback(Pageable pageable) {
        return feedbackRepository.findAllRead(pageable)
                .map(FeedbackMapper::mapToFeedbackModel);
    }

    @Override
    public void replyFeedback(String recipient, String body, String subject) {
        if (StringUtils.hasText(recipient) && StringUtils.hasText(body)) {
            emailService.sendMails(recipient.trim(),"Chèr(e) client(e)",subject,body);
        }
    }

    @Override
    public Long countFeedback() {
        return feedbackRepository.countUnreadFeedback();
    }
}
