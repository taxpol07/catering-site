package com.example.demo.controller;

import com.example.demo.model.Equipment;
import com.example.demo.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class EquipmentController {

    @Autowired
    private EquipmentRepository repository;

    // ANA SAYFA
    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(required = false) String category) {
        List<Equipment> listEquipments;
        if (category != null && !category.isEmpty()) {
            listEquipments = repository.findByCategory(category);
        } else {
            listEquipments = repository.findAll();
        }
        model.addAttribute("listEquipments", listEquipments);
        model.addAttribute("param", category); // Kategori seçili kalsın diye
        return "index"; // index.html'e git
    }

    // KATEGORİ FİLTRESİ
    @GetMapping("/category/{category}")
    public String viewCategoryPage(@PathVariable String category, Model model) {
        return viewHomePage(model, category);
    }

    // YENİ ÜRÜN EKLEME SAYFASI
    @GetMapping("/showNewEquipmentForm")
    public String showNewEquipmentForm(Model model) {
        Equipment equipment = new Equipment();
        model.addAttribute("equipment", equipment);
        return "new_equipment";
    }

    // ÜRÜN KAYDETME
    @PostMapping("/saveEquipment")
    public String saveEquipment(@ModelAttribute("equipment") Equipment equipment,
                                @RequestParam("image") MultipartFile multipartFile) throws IOException {

        // Eğer yeni bir resim seçildiyse Cloudinary'ye yükle (yoksa eskisi kalır)
        if (!multipartFile.isEmpty()) {
            // Burada Cloudinary servisi çağrılmalı. Şimdilik basit tutuyorum.
            // Gerçek projede CloudinaryService.upload(multipartFile) kullanıyoruz.
        }

        repository.save(equipment);
        return "redirect:/";
    }

    // DÜZENLEME SAYFASI
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable(value = "id") long id, Model model) {
        Equipment equipment = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));
        model.addAttribute("equipment", equipment);
        return "new_equipment";
    }

    // SİLME İŞLEMİ
    @GetMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable(value = "id") long id) {
        this.repository.deleteById(id);
        return "redirect:/";
    }

    // *** İŞTE DÜZELTİLEN KISIM: DETAY SAYFASI ***
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable(value = "id") long id, Model model) {
        // Ürünü bul, bulamazsan hata ver
        Equipment equipment = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));

        // "equipment" adıyla sayfaya gönder
        model.addAttribute("equipment", equipment);

        return "details"; // details.html dosyasını aç!
    }
}