package com.ecommerce.backend.upload;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/uploads")
public class ProductImageUploadController {

    private final ProductImageStorageService storageService;

    public ProductImageUploadController(
            ProductImageStorageService storageService
    ) {
        this.storageService = storageService;
    }

    @PostMapping(
            value = "/product-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductImageUploadResponse> uploadProductImage(
            @RequestPart("file") MultipartFile file
    ) {

        ProductImageUploadResponse response =
                storageService.store(file);

        return ResponseEntity.ok(response);
    }
}