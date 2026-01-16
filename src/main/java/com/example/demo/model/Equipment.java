package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String brand;
    private String model;

    @Column(nullable = false)
    private double price;

    private String testingStatus;

    @Column(length = 2000)
    private String technicalSpecs;

    // Kapak resmi (liste sayfası için)
    private String imagePath;

    // Detay sayfası carousel için
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "equipment_images",
            joinColumns = @JoinColumn(name = "equipment_id")
    )
    @Column(name = "image_name")
    private List<String> additionalImages = new ArrayList<>();

    public Equipment() {}

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getTestingStatus() { return testingStatus; }
    public void setTestingStatus(String testingStatus) { this.testingStatus = testingStatus; }

    public String getTechnicalSpecs() { return technicalSpecs; }
    public void setTechnicalSpecs(String technicalSpecs) { this.technicalSpecs = technicalSpecs; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public List<String> getAdditionalImages() { return additionalImages; }
    public void setAdditionalImages(List<String> additionalImages) { this.additionalImages = additionalImages; }
}
