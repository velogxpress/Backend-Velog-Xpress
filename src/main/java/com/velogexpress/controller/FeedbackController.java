package com.velogexpress.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.velogexpress.model.FeedbackModel;
import com.velogexpress.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    // ✅ Create new feedback
    @CacheEvict(cacheNames = "feedback", allEntries = true)
    @PostMapping
    public ResponseEntity<FeedbackModel> createFeedback(@RequestBody FeedbackModel feedbackModel) {
        FeedbackModel feedback = feedbackService.createFeedback(feedbackModel);
        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }

    // ✅ Get all feedback (paginated)
    @Cacheable(cacheNames = "feedback")
    @GetMapping
    public ResponseEntity<Page<FeedbackModel>> getAllFeedback(Pageable pageable) {
        Page<FeedbackModel> feedbackList = feedbackService.getAllFeedback(pageable);
        return ResponseEntity.ok(feedbackList);
    }

    // ✅ Get feedback by user email
    @Cacheable(cacheNames = "feedback")
    @GetMapping("/user/{email}")
    public ResponseEntity<Page<FeedbackModel>> getFeedbackByUser(
            @PathVariable("email") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<FeedbackModel> feedbackPage = feedbackService.getFeedbackByUser(email, PageRequest.of(page, size));
        return ResponseEntity.ok(feedbackPage);
    }

    // ✅ Update feedback (mark as read)
    @CacheEvict(cacheNames = "feedback", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackModel> updateFeedback(@PathVariable("id") Long id) {
        FeedbackModel model = feedbackService.updateFeedback(id);
        if (model == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(model);
    }

    // ✅ Get all read feedback (paginated)
    @Cacheable(cacheNames = "feedback")
    @GetMapping("/read")
    public ResponseEntity<Page<FeedbackModel>> getReadFeedback(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<FeedbackModel> all = feedbackService.getReadFeedback(PageRequest.of(page, size));
        return ResponseEntity.ok(all);
    }

    // ✅ Get all unread feedback (paginated)
    @Cacheable(cacheNames = "feedback")
    @GetMapping("/unread")
    public ResponseEntity<Page<FeedbackModel>> getUnreadFeedback(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<FeedbackModel> all = feedbackService.getUnreadFeedback(PageRequest.of(page, size));
        return ResponseEntity.ok(all);
    }

    // ✅ Count unread feedback
    @Cacheable(cacheNames = "feedback")
    @GetMapping("/count")
    public ResponseEntity<Long> countUnreadFeedback() {
        return ResponseEntity.ok(feedbackService.countFeedback());
    }

    // ✅ Send reply via email (use query params or body, not path vars)
    @CacheEvict(cacheNames = "feedback", allEntries = true)
    @PostMapping("/reply")
    public ResponseEntity<String> replyFeedback(
            @RequestParam String recipient,
            @RequestParam String subject,
            @RequestParam String body
    ) {
        feedbackService.replyFeedback(recipient, body, subject);
        return ResponseEntity.ok("Email envoyé avec succès.");
    }

    @Cacheable(cacheNames = "feedback")
    @GetMapping("/feedbackList/{description}")
    public ResponseEntity<Page> getFeedbackList(@PathVariable("description") String description,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "25") int size
                                                ){
        Page<FeedbackModel> FeedbackModel=feedbackService.getFeedbackByEmail(description, PageRequest.of(page, size));
        return ResponseEntity.ok(FeedbackModel);
    }
}
