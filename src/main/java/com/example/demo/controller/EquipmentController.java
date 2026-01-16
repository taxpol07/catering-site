package com.example.demo.controller;

import com.example.demo.model.Equipment;
import com.example.demo.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    // *** DÜZELTİLEN KISIM: ÜRÜN KAYDETME ***
    @PostMapping("/saveEquipment")
    public String saveEquipment(@ModelAttribute("equipment") Equipment equipment,
                                @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {

        // Çoklu resim yönetimi için hazırlık:
        // HTML formundaki name="imageFiles" ile buradaki @RequestParam("imageFiles") eşleşmeli.

        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                // TODO: Buraya Cloudinary yükleme kodunuz gelecek.
                // Örnek mantık:
                // String imageUrl = cloudinaryService.upload(file);

                // Eğer ana resim boşsa ilk resmi ana resim yap:
                // if (equipment.getImagePath() == null) {
                //     equipment.setImagePath(imageUrl);
                // } else {
                //     equipment.getAdditionalImages().add(imageUrl);
                // }
            }
        }

        // Şimdilik sadece veritabanına kaydediyoruz (Resim yükleme servisini eklediğinizde yukarıyı açarsınız)
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