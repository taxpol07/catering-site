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
    private Cloudinary cloudinary;

    // 🔹 INDEX
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("listEquipments", repository.findAll());
        return "index";
    }

    // 🔹 NEW ITEM FORM
    @GetMapping("/showNewEquipmentForm")
    public String showForm(Model model) {
        model.addAttribute("equipment", new Equipment());
        return "new_equipment";
    }

    // 🔹 EDIT ITEM FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Equipment existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));
        model.addAttribute("equipment", existing);
        return "new_equipment";
    }

    // 🔹 SAVE / UPDATE EQUIPMENT (Cloudinary Versiyonu)
    @PostMapping("/saveEquipment")
    public String save(
            @ModelAttribute Equipment eq,
            @RequestParam(value = "imageFiles", required = false) MultipartFile[] files
    ) throws IOException {

        List<String> imageUrls = new ArrayList<>();

        // Eğer dosya seçildiyse Cloudinary'ye yükle
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isEmpty()) {
                    // Cloudinary'ye yükleme işlemi
                    Map uploadResult = cloudinary.uploader().upload(files[i].getBytes(), ObjectUtils.asMap("resource_type", "auto"));

                    // Bize dönen güvenli resim linkini (https://...) alıyoruz
                    String url = (String) uploadResult.get("secure_url");

                    if (i == 0) {
                        eq.setImagePath(url); // İlk resim kapak resmi (URL olarak kaydediyoruz)
                    }
                    imageUrls.add(url);
                }
            }
        }

        // Ekstra resimler varsa ekle
        if (!imageUrls.isEmpty()) {
            eq.setAdditionalImages(imageUrls);
        }

        repository.save(eq);
        return "redirect:/";
    }

    // 🔹 DETAILS
    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("equipment", repository.findById(id).orElseThrow());
        return "details";
    }

    // 🔹 DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/";
    }

    // 🔹 CATEGORY FILTER
    @GetMapping("/category/{name}")
    public String category(@PathVariable String name, Model model) {
        model.addAttribute("listEquipments",
                repository.findAll().stream()
                        .filter(e -> e.getCategory().equalsIgnoreCase(name))
                        .toList()
        );
        return "index";
    }
}