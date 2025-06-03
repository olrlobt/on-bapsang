package com.on_bapsang.backend.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID() + "." + fileExtension;

        try {
            // S3 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

            // Presigned URL 생성 후 반환
            return generatePresignedUrl(fileName, 10);

        } catch (Exception e) {
            // 예외 발생 시 업로드한 파일 삭제 시도
            if (amazonS3.doesObjectExist(bucket, fileName)) {
                amazonS3.deleteObject(bucket, fileName);
            }
            throw new RuntimeException("S3 업로드 중 오류가 발생하여 업로드된 파일을 삭제했습니다.", e);
        }
    }

    private String generatePresignedUrl(String fileName, int expireMinutes) {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000L * 60 * expireMinutes);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(request);
        return url.toString();
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            throw new IllegalArgumentException("파일 확장자가 없습니다.");
        }
        return filename.substring(dotIndex + 1);
    }
}
