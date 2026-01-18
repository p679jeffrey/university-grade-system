package com.university.gradesystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.materials-dir:uploads/materials}")
    private String materialsDir;

    @Value("${file.submissions-dir:uploads/submissions}")
    private String submissionsDir;

    /**
     * 儲存教材檔案
     */
    public String storeMaterial(MultipartFile file) throws IOException {
        return storeFile(file, materialsDir);
    }

    /**
     * 儲存作業提交檔案
     */
    public String storeSubmission(MultipartFile file) throws IOException {
        return storeFile(file, submissionsDir);
    }

    /**
     * 通用檔案儲存方法
     */
    private String storeFile(MultipartFile file, String directory) throws IOException {
        // 建立目錄
        Path uploadPath = Paths.get(directory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一檔名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // 儲存檔案
        Path targetLocation = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return directory + "/" + uniqueFilename;
    }

    /**
     * 載入檔案
     */
    public Resource loadFile(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("找不到檔案: " + filePath);
        }
    }

    /**
     * 刪除檔案
     */
    public void deleteFile(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        Files.deleteIfExists(file);
    }
}