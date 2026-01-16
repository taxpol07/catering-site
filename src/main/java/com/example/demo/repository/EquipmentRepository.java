package com.example.demo.repository;

import com.example.demo.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // Bu etiket Spring'in bu dosyayı bulmasını sağlar
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    // Kategoriye göre filtreleme metodu
    List<Equipment> findByCategory(String category);
}