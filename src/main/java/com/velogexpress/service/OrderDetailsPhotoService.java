package com.velogexpress.service;

import com.velogexpress.model.OrderDetailsPhotoModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrderDetailsPhotoService {
    OrderDetailsPhotoModel addPhoto(Long orderDetailsId, MultipartFile file);
    List<OrderDetailsPhotoModel> getPhotos(Long orderDetailsId);
    void deletePhoto(Long id);
}
