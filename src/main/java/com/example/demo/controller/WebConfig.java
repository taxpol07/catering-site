package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.properties dosyasındaki 'app.upload.dir' yolunu buraya alıyoruz
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Yolu normalize et (Windows/Mac uyumu için)
        Path uploadPath = Paths.get(uploadDir);
        String uploadAbsolutePath = uploadPath.toFile().getAbsolutePath();

        // '/image/**' isteği gelince, gidip o klasöre bakmasını söylüyoruz.
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + uploadAbsolutePath + "/");
    }
}