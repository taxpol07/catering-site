package com.example.demo.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Artık resimleri Cloudinary (Bulut) tutuyor.
    // O yüzden eski yerel klasör ayarlarına ihtiyacımız kalmadı.
    // Bu dosya şimdilik boş duracak.
}