package com.example.demo.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.model.Equipment;
import com.example.demo.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class EquipmentController {

    @Autowired
    private EquipmentRepository repository;

    @Autowired
    private Cloudinary cloudinary; // Cloudinary servisini çağırıyoruz

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

    // *** CLOUDINARY ENTEGRASYONLU KAYIT İŞLEMİ ***
    @PostMapping("/saveEquipment")
    public String saveEquipment(@ModelAttribute("equipment") Equipment equipment,
                                @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {

        // Null kontrolü ve liste başlatma
        if (equipment.getAdditionalImages() == null) {
            equipment.setAdditionalImages(new ArrayList<>());
        }

        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    try {
                        // 1. Resmi Cloudinary'ye yükle (Otomatik format algılama ile)
                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));

                        // 2. Yüklenen resmin kalıcı internet adresini (URL) al
                        String imageUrl = (String) uploadResult.get("url");

                        // 3. Veritabanı objesine bu URL'yi ata
                        if (equipment.getImagePath() == null || equipment.getImagePath().isEmpty()) {
                            // İlk resim her zaman "Kapak Resmi" olsun
                            equipment.setImagePath(imageUrl);
                        } else {
                            // Diğer resimler "Ek Resimler" listesine
                            equipment.getAdditionalImages().add(imageUrl);
                        }
                    } catch (IOException e) {
                        // Hata durumunda log bas ve işlemi durdurma (veya kullanıcıya hata dönülebilir)
                        System.err.println("Cloudinary yükleme hatası: " + e.getMessage());
                    }
                }
            }
        }

        // Veritabanına kaydet
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

    // DETAY SAYFASI
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable(value = "id") long id, Model model) {
        Equipment equipment = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));

        model.addAttribute("equipment", equipment);
        return "details";
    }
}