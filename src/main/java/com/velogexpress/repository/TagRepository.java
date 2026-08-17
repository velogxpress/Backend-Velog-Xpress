package com.velogexpress.repository;

import com.velogexpress.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(value = "SELECT * FROM tag WHERE qrcode=?1", nativeQuery = true)
    Tag findTag(String tag);
}
