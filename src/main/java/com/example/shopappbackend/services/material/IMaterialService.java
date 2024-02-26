package com.example.shopappbackend.services.material;

import com.example.shopappbackend.dtos.MaterialDTO;
import com.example.shopappbackend.entities.Material;
import com.example.shopappbackend.exceptions.DataNotFoundException;

import java.util.List;

public interface IMaterialService {
    Material getMaterialById(Long id) throws DataNotFoundException;
    List<Material> getMaterials();
    Material createMaterial(MaterialDTO materialDTO);
    void updateMaterial(Long id, MaterialDTO materialDTO) throws DataNotFoundException;
    void deleteMaterialById(Long id) throws DataNotFoundException;
}
