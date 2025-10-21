package com.circuitguard.ai.usermanagement.azure.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.circuitguard.ai.usermanagement.azure.service.BlobStorageService;
import com.circuitguard.ai.usermanagement.model.MediaModel;

@Service
public class AzureBlobStorageService implements BlobStorageService {

    private static final Logger log = LoggerFactory.getLogger(AzureBlobStorageService.class);

    private final BlobContainerClient containerClient;

    public AzureBlobStorageService(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
        if (!containerClient.exists()) {
            throw new IllegalStateException("Azure Blob container does not exist: " + containerClient.getBlobContainerName());
        }
    }

    @Override
    public MediaModel uploadFile(MultipartFile file) throws IOException {
        return uploadToBlob(file.getOriginalFilename(), file);
    }

    @Override
    public List<MediaModel> uploadFiles(List<MultipartFile> files) throws IOException {
        List<MediaModel> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadedFiles.add(uploadFile(file));
        }
        return uploadedFiles;
    }

    @Override
    public MediaModel uploadCustomerPictureFile(Long userId, MultipartFile file) throws IOException {
        String safeFileName = String.format("customer_%d_%s", userId, file.getOriginalFilename());
        return uploadToBlob(safeFileName, file);
    }

    private MediaModel uploadToBlob(String fileName, MultipartFile file) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        BlobClient blobClient = containerClient.getBlobClient(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
        }

        blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(file.getContentType()));

        log.info("Uploaded file '{}' to Azure Blob Storage: {}", fileName, blobClient.getBlobUrl());
        return new MediaModel(fileName, blobClient.getBlobUrl());
    }
}
