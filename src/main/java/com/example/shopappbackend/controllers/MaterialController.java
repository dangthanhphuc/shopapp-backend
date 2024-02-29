package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.MaterialDTO;
import com.example.shopappbackend.entities.Material;
import com.example.shopappbackend.responses.material.MaterialResponse;
import com.example.shopappbackend.services.material.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/materials")
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MaterialResponse> createMaterial(
            @Valid @RequestBody MaterialDTO materialDTO,
            BindingResult result
    ){
        MaterialResponse materialResponse = new MaterialResponse();
        if(result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            materialResponse.setErrors(errors);
            materialResponse.setMessage("Create material is failed");
            return ResponseEntity.badRequest().body(materialResponse);
        }
        Material material = materialService.createMaterial(materialDTO);
        materialResponse.setMaterial(material);
        materialResponse.setMessage("Create material is successfully");
        return ResponseEntity.ok(materialResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateMaterial(
            @PathVariable Long id,
            @RequestBody MaterialDTO materialDTO
    ){
        try {
            materialService.updateMaterial(id, materialDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Successfully updated material");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteMaterial(
            @PathVariable Long id
    ){
        try{
            materialService.deleteMaterialById(id);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Successfully updated material");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable("id") Long materialId){
        try {
            Material existingMaterial = materialService.getMaterialById(materialId);
            return ResponseEntity.ok(existingMaterial);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("")
    public ResponseEntity<List<Material>> getAllMaterials(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Material> categories = materialService.getMaterials();
        return ResponseEntity.ok(categories);
    }
    
}
