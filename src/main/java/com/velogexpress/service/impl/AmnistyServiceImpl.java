package com.velogexpress.service.impl;

import com.velogexpress.entity.Amnisty;
import com.velogexpress.entity.OrderDetails;
import com.velogexpress.mapper.AmnistyMapper;
import com.velogexpress.mapper.OrderDetailsMapper;
import com.velogexpress.model.AmnistyModel;
import com.velogexpress.repository.AmnistyRepository;
import com.velogexpress.service.AmnistyService;
import com.velogexpress.tools.SKU;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmnistyServiceImpl implements AmnistyService {
    private final AmnistyRepository amnistyRepository;
    @Value("${file.upload-dir}") private String uploadDir;
    @Override
    public AmnistyModel createAmnesty(MultipartFile file, AmnistyModel amnistyModel) {
        Amnisty amnisty = AmnistyMapper.mapToAmnisty(amnistyModel);
        String cleanOriginalName = Paths
                .get(Objects.requireNonNull(file.getOriginalFilename()))
                .getFileName()
                .toString();

        String extension = "";
        if (cleanOriginalName.contains(".")) {
            extension = cleanOriginalName.substring(cleanOriginalName.lastIndexOf("."));
        }

        // 2️⃣ Nom temporaire SAFE
        String tempFileName =
                "upload_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        // 3️⃣ Chemin temporaire
        String tempFilePath =
                System.getProperty("java.io.tmpdir") + File.separator + tempFileName;

        // 4️⃣ Transfer vers temp
        File tempFile = new File(tempFilePath);
        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Erreur transfert fichier", e);
        }

        // 5️⃣ Dossier final
        File uploadPath = new File(uploadDir + "/products/");
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        // 6️⃣ Nouveau nom FINAL
        String finalFileName =
                System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        try {
            BufferedImage bufferedImage = ImageIO.read(tempFile);

            File outputFile = new File(uploadPath, finalFileName);
            ImageIO.write(bufferedImage, extension.replace(".", ""), outputFile);

            // 7️⃣ Entity Image
            amnisty.setPicture(finalFileName);
            if(amnistyModel.getTracking()==null) {
                SKU sku = new SKU();
                amnisty.setTracking(sku.AMNISTYCODE());
            }

            // 8️⃣ Save
            Amnisty saved = amnistyRepository.save(amnisty);

            // 9️⃣ Nettoyage temp
            tempFile.delete();

            return AmnistyMapper.mapToAmnistyModel(saved);

        } catch (IOException e) {
            throw new RuntimeException("Erreur sauvegarde image", e);
        }
    }

    @Override
    public Page<AmnistyModel> getAllAmnisty(Pageable pageable) {
        Page<Amnisty> amnistys = amnistyRepository.findAllByOrderIdDesc(pageable);
        return amnistys.map(AmnistyMapper::mapToAmnistyModel);
    }

    @Override
    public List<AmnistyModel> getAllAmnisty() {
        List<Amnisty> amnistyList=amnistyRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
       return amnistyList.stream().map(
                AmnistyMapper::mapToAmnistyModel
        ).collect(Collectors.toList());
    }

    @Override
    public Page<AmnistyModel> searchAmnisty(String param, Pageable pageable) {
        Page<Amnisty> amnistys = amnistyRepository.search(param,pageable);
        return amnistys.map(AmnistyMapper::mapToAmnistyModel);
    }

    @Override
    public List<AmnistyModel> searchAmnisty(String param) {
        List<Amnisty> amnistyList=amnistyRepository.searchAmnisty(param);
        return amnistyList.stream().map(
                AmnistyMapper::mapToAmnistyModel
        ).collect(Collectors.toList());
    }

    @Override
    public AmnistyModel updateAmnisty(String upc, AmnistyModel amnistyModel) {
        Amnisty amnisty=amnistyRepository.findByTracking(upc);
        if(amnisty==null){
            return null;
        }else{
            amnisty.setStatus(amnistyModel.getStatus());
            Amnisty saved = amnistyRepository.save(amnisty);
            return AmnistyMapper.mapToAmnistyModel(saved);
        }
    }
}
