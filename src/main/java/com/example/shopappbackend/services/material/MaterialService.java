package com.example.shopappbackend.services.material;

import com.example.shopappbackend.dtos.MaterialDTO;
import com.example.shopappbackend.entities.Material;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService implements IMaterialService{
    private final MaterialRepository materialRepository;
    @Override
    public Material getMaterialById(Long id) throws DataNotFoundException {
        return materialRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Material not found")
        );
    }

    @Override
    public List<Material> getMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public Material createMaterial(MaterialDTO materialDTO) {
        Material material = Material.builder()
                .name(materialDTO.getName())
                .build();
        return materialRepository.save(material);
    }

    @Override
    public void updateMaterial(Long id, MaterialDTO materialDTO) throws DataNotFoundException {
        Material exsitingMaterial = materialRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Material not found")
        );
        exsitingMaterial.setName(materialDTO.getName());
        exsitingMaterial.setUpdatedAt(LocalDateTime.now());
        materialRepository.save(exsitingMaterial);
    }

    @Override
    public void deleteMaterialById(Long id) throws DataNotFoundException {
        Material exsitingMaterial = materialRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Material not found")
        );
        exsitingMaterial.setDeleted(true);
        exsitingMaterial.setUpdatedAt(LocalDateTime.now());
        materialRepository.save(exsitingMaterial);
    }
}
