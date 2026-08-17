package com.velogexpress.repository;

import com.velogexpress.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = "SELECT * FROM feedback WHERE feedback_email = ?1 ORDER BY id DESC", nativeQuery = true)
    Page<Feedback> findByEmailOrderByIdDesc(String email, Pageable pageable);

    @Query(value = """
        SELECT f.* FROM feedback f
        INNER JOIN (
            SELECT feedback_email, MAX(id) AS max_id
            FROM feedback
            GROUP BY feedback_email
        ) latest ON f.id = latest.max_id
        ORDER BY f.id DESC
        """, nativeQuery = true)
    Page<Feedback> findLatestFeedbackPerEmail(Pageable pageable);

    @Query(value = "SELECT * FROM feedback WHERE id = ?1", nativeQuery = true)
    Feedback findByIdNative(Long id);

    @Query(value = "SELECT * FROM feedback WHERE feedback_status = 'U/R' ORDER BY id DESC", nativeQuery = true)
    Page<Feedback> findAllUnread(Pageable pageable);

    @Query(value = "SELECT * FROM feedback WHERE feedback_status = 'READ' ORDER BY id DESC", nativeQuery = true)
    Page<Feedback> findAllRead(Pageable pageable);

    @Query(value = "SELECT COUNT(id) FROM feedback WHERE feedback_status = 'U/R'", nativeQuery = true)
    long countUnreadFeedback();
}
