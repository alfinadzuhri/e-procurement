package com.enigma.group5.e_procurement.repository;

import com.enigma.group5.e_procurement.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}
