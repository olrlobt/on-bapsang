//package com.on_bapsang.backend.util;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class ImageUploader {
//
//    private final AmazonS3 amazonS3;
//    private final String bucket = "your-bucket-name"; // 실제 버킷 이름으로 교체
//
//    public String upload(MultipartFile file) {
//        if (file.isEmpty()) {
//            throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
//        }
//
//        String originalFilename = file.getOriginalFilename();
//        String fileExtension = getFileExtension(originalFilename);
//        String fileName = UUID.randomUUID() + "." + fileExtension;
//
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//            metadata.setContentType(file.getContentType());
//
//            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
//        } catch (IOException e) {
//            throw new RuntimeException("S3 업로드 중 오류 발생", e);
//        }
//
//        return amazonS3.getUrl(bucket, fileName).toString(); // 업로드된 이미지의 전체 URL 반환
//    }
//
//    private String getFileExtension(String filename) {
//        int dotIndex = filename.lastIndexOf('.');
//        if (dotIndex == -1) {
//            throw new IllegalArgumentException("파일 확장자가 없습니다.");
//        }
//        return filename.substring(dotIndex + 1);
//    }
//}

// 임시용
package com.on_bapsang.backend.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageUploader {

    // S3 없이 개발 중이므로, 실제 업로드는 생략하고 더미 URL만 반환
    public String upload(MultipartFile file) {
        // 파일이 비어있으면 예외 처리 (원래 로직 유지 가능)
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
        }

        // 실제 업로드 생략, 더미 이미지 URL 반환
        return "https://example.com/dummy.jpg";
    }
}
