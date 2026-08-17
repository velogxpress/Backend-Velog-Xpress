package com.velogexpress.service;

import com.velogexpress.model.AmnistyModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AmnistyService {
    AmnistyModel createAmnesty(MultipartFile file,AmnistyModel amnistyModel);
    Page<AmnistyModel> getAllAmnisty(Pageable pageable);
    List<AmnistyModel> getAllAmnisty();
    Page<AmnistyModel> searchAmnisty(String param,Pageable pageable);
    List<AmnistyModel> searchAmnisty(String param);
    AmnistyModel updateAmnisty(String upc,AmnistyModel amnistyModel);

}
