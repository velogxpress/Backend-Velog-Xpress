package com.velogexpress.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDetailsPhotoSchemaInitializer {
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void createTableIfNeeded() {
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS order_details_photos (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    orderdetails_id BIGINT NOT NULL,
                    photo VARCHAR(255) NOT NULL,
                    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT fk_order_details_photos_orderdetails
                        FOREIGN KEY (orderdetails_id) REFERENCES order_details(id)
                        ON DELETE CASCADE
                )
                """);
    }
}
