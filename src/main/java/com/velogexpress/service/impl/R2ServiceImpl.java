package com.velogexpress.service.impl;

import com.velogexpress.service.R2Service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

/**
 * Uploads/serves files through Cloudflare R2 using the S3-compatible API.
 * Replaces the old local-disk uploads/products storage.
 */
@Service
public class R2ServiceImpl implements R2Service {

    @Value("${r2.account-id}")
    private String accountId;

    @Value("${r2.access-key-id}")
    private String accessKeyId;

    @Value("${r2.secret-access-key}")
    private String secretAccessKey;

    @Value("${r2.bucket-name}")
    private String bucketName;

    @Value("${r2.public-url}")
    private String publicUrlBase;

    private S3Client s3Client;

    @PostConstruct
    private void init() {
        s3Client = S3Client.builder()
                .endpointOverride(URI.create("https://" + accountId + ".r2.cloudflarestorage.com"))
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccess(true)
                        .build())
                .build();
    }

    @Override
    public String upload(byte[] data, String key, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(data));
        return publicUrl(key);
    }

    @Override
    public String publicUrl(String key) {
        String base = publicUrlBase.endsWith("/")
                ? publicUrlBase.substring(0, publicUrlBase.length() - 1)
                : publicUrlBase;
        String cleanKey = key.startsWith("/") ? key.substring(1) : key;
        return base + "/" + cleanKey;
    }
}
