package com.example.demo.controller;

import com.example.demo.model.Equipment;
import com.example.demo.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        model.addAttribute("param", category);
        return "index";
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

    // *** DÜZELTİLEN VE TAMAMLANAN KISIM: ÜRÜN + RESİM KAYDETME ***
    @PostMapping("/saveEquipment")
    public String saveEquipment(@ModelAttribute("equipment") Equipment equipment,
                                @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {

        // Eğer listede dosya varsa işlemi başlat
        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                // Dosya boş değilse (kullanıcı bir şey seçtiyse)
                if (!file.isEmpty()) {

                    // 1. Dosya ismini benzersiz yap (Örn: 1735654_resim.jpg)
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                    // 2. Kayıt yapılacak klasörü belirle: Proje ana dizinindeki "uploads"
                    Path uploadPath = Paths.get("uploads");

                    // Klasör yoksa oluştur
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // 3. Dosyayı kaydet
                    try (InputStream inputStream = file.getInputStream()) {
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                        // 4. Veritabanı objesine dosya adını ekle
                        if (equipment.getImagePath() == null) {
                            // İlk resim her zaman "Kapak Resmi" olsun
                            equipment.setImagePath(fileName);
                        } else {
                            // Diğer resimler "Ek Resimler" listesine
                            equipment.getAdditionalImages().add(fileName);
                        }
                    } catch (IOException ioe) {
                        throw new IOException("Resim kaydedilemedi: " + fileName, ioe);
                    }
                }
            }
        }

        // Veritabanına kaydet
        repository.save(equipment);

        // Ana sayfaya yönlendir
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

    // DETAY SAYFASI
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable(value = "id") long id, Model model) {
        Equipment equipment = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));

        model.addAttribute("equipment", equipment);
        return "details";
    }
}