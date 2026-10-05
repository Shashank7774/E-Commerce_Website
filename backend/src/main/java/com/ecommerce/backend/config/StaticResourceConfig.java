package com.ecommerce.backend.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private final String uploadDirectory;

    public StaticResourceConfig(
            @Value("${app.upload.product-image-dir}")
            String uploadDirectory
    ) {
        this.uploadDirectory = uploadDirectory;
    }

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        Path uploadPath =
                Paths.get(uploadDirectory)
                        .toAbsolutePath()
                        .normalize();

        registry.addResourceHandler(
                "/uploads/products/**"
        ).addResourceLocations(
                uploadPath.toUri().toString()
        );
    }
}