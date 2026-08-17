package com.velogexpress.service.impl;

import com.velogexpress.entity.OrderDetails;
import com.velogexpress.model.OrderDetailsPhotoModel;
import com.velogexpress.repository.OrderDetailsRepository;
import com.velogexpress.service.OrderDetailsPhotoService;
import com.velogexpress.service.R2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDetailsPhotoServiceImpl implements OrderDetailsPhotoService {
    private final OrderDetailsRepository orderDetailsRepository;
    private final JdbcTemplate jdbcTemplate;
    private final R2Service r2Service;

    @Value("${file.upload-dir}") private String uploadDir;

    @Override
    public OrderDetailsPhotoModel addPhoto(Long orderDetailsId, MultipartFile file) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId).orElse(null);
        if (orderDetails == null || file == null || file.isEmpty()) {
            return null;
        }

        String cleanOriginalName = Paths
                .get(file.getOriginalFilename() == null ? "photo.png" : file.getOriginalFilename())
                .getFileName()
                .toString();

        String extension = ".png";
        if (cleanOriginalName.contains(".")) {
            extension = cleanOriginalName.substring(cleanOriginalName.lastIndexOf("."));
        }

        String finalFileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        try {
            r2Service.upload(file.getBytes(), "products/" + finalFileName, file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Erreur sauvegarde photo galerie", e);
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO order_details_photos (orderdetails_id, photo) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setLong(1, orderDetails.getId());
            statement.setString(2, finalFileName);
            return statement;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return getPhoto(id);
    }

    @Override
    public List<OrderDetailsPhotoModel> getPhotos(Long orderDetailsId) {
        return jdbcTemplate.query(
                "SELECT id, orderdetails_id, photo, created_at FROM order_details_photos WHERE orderdetails_id=? ORDER BY id DESC",
                (rs, rowNum) -> new OrderDetailsPhotoModel(
                        rs.getLong("id"),
                        rs.getLong("orderdetails_id"),
                        rs.getString("photo"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ),
                orderDetailsId
        );
    }

    @Override
    public void deletePhoto(Long id) {
        jdbcTemplate.update("DELETE FROM order_details_photos WHERE id=?", id);
    }

    private OrderDetailsPhotoModel getPhoto(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, orderdetails_id, photo, created_at FROM order_details_photos WHERE id=?",
                (rs, rowNum) -> {
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    return new OrderDetailsPhotoModel(
                            rs.getLong("id"),
                            rs.getLong("orderdetails_id"),
                            rs.getString("photo"),
                            createdAt != null ? createdAt.toLocalDateTime() : null
                    );
                },
                id
        );
    }
}
