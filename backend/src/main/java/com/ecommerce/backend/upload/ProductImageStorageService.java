package com.ecommerce.backend.upload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductImageStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            );

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(
                    "jpg",
                    "jpeg",
                    "png",
                    "webp"
            );

    private final Path uploadDirectory;

    private final long maxFileSize;

    public ProductImageStorageService(
            @Value("${app.upload.product-image-dir}")
            String uploadDirectory,

            @Value("${app.upload.max-product-image-size}")
            long maxFileSize
    ) {

        this.uploadDirectory =
                Paths.get(uploadDirectory)
                        .toAbsolutePath()
                        .normalize();

        this.maxFileSize =
                maxFileSize;

        try {

            Files.createDirectories(
                    this.uploadDirectory
            );

        } catch (IOException exception) {

            throw new FileUploadException(
                    "Unable to initialize product image storage.",
                    exception
            );
        }
    }

    // =========================================================
    // STORE IMAGE
    // =========================================================

    public ProductImageUploadResponse store(
            MultipartFile file
    ) {

        validateFile(file);

        String originalFileName =
                StringUtils.cleanPath(
                        file.getOriginalFilename() == null
                                ? ""
                                : file.getOriginalFilename()
                );

        String extension =
                getExtension(
                        originalFileName
                );

        String generatedFileName =
                UUID.randomUUID()
                        + "."
                        + extension;

        Path targetPath =
                uploadDirectory
                        .resolve(generatedFileName)
                        .normalize();

        // Prevent path traversal.
        if (
                !targetPath.startsWith(
                        uploadDirectory
                )
        ) {

            throw new FileUploadException(
                    "Invalid file path."
            );
        }

        try (InputStream inputStream =
                     file.getInputStream()) {

            Files.copy(
                    inputStream,
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException exception) {

            throw new FileUploadException(
                    "Unable to store product image.",
                    exception
            );
        }

        String imageUrl =
                "/uploads/products/"
                        + generatedFileName;

        return new ProductImageUploadResponse(
                imageUrl,
                generatedFileName
        );
    }

    // =========================================================
    // DELETE IMAGE
    // =========================================================

    public void deleteByImageUrl(
            String imageUrl
    ) {

        if (
                imageUrl == null ||
                imageUrl.isBlank()
        ) {
            return;
        }

        String prefix =
                "/uploads/products/";

        if (
                !imageUrl.startsWith(prefix)
        ) {
            return;
        }

        String fileName =
                imageUrl
                        .substring(prefix.length())
                        .trim();

        if (
                fileName.isBlank()
        ) {
            return;
        }

        // Only allow a generated filename.
        // No directory separators are allowed.
        if (
                fileName.contains("/") ||
                fileName.contains("\\") ||
                fileName.contains("..")
        ) {
            throw new FileUploadException(
                    "Invalid image path."
            );
        }

        String extension =
                getExtension(fileName);

        if (
                extension == null ||
                !ALLOWED_EXTENSIONS
                        .contains(extension)
        ) {
            throw new FileUploadException(
                    "Invalid image file."
            );
        }

        Path imagePath =
                uploadDirectory
                        .resolve(fileName)
                        .normalize();

        // Final path traversal protection.
        if (
                !imagePath.startsWith(
                        uploadDirectory
                )
        ) {
            throw new FileUploadException(
                    "Invalid image path."
            );
        }

        try {

            Files.deleteIfExists(
                    imagePath
            );

        } catch (IOException exception) {

            throw new FileUploadException(
                    "Unable to delete product image.",
                    exception
            );
        }
    }

    // =========================================================
    // VALIDATE FILE
    // =========================================================

    private void validateFile(
            MultipartFile file
    ) {

        if (
                file == null ||
                file.isEmpty()
        ) {

            throw new FileUploadException(
                    "Product image is required."
            );
        }

        if (
                file.getSize()
                        > maxFileSize
        ) {

            throw new FileUploadException(
                    "Product image must not exceed 5 MB."
            );
        }

        String contentType =
                file.getContentType();

        if (
                contentType == null ||
                !ALLOWED_CONTENT_TYPES
                        .contains(
                                contentType.toLowerCase(
                                        Locale.ROOT
                                )
                        )
        ) {

            throw new FileUploadException(
                    "Only JPG, PNG and WEBP images are allowed."
            );
        }

        String originalFileName =
                file.getOriginalFilename();

        String extension =
                getExtension(
                        originalFileName
                );

        if (
                extension == null ||
                !ALLOWED_EXTENSIONS
                        .contains(
                                extension
                        )
        ) {

            throw new FileUploadException(
                    "Invalid image file extension."
            );
        }
    }

    // =========================================================
    // FILE EXTENSION
    // =========================================================

    private String getExtension(
            String fileName
    ) {

        if (
                fileName == null ||
                fileName.isBlank()
        ) {
            return null;
        }

        int lastDot =
                fileName.lastIndexOf('.');

        if (
                lastDot < 0 ||
                lastDot ==
                        fileName.length() - 1
        ) {
            return null;
        }

        return fileName
                .substring(lastDot + 1)
                .toLowerCase(
                        Locale.ROOT
                );
    }
}