package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.MaterialDTO;
import com.example.shopappbackend.entities.Material;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.responses.ResponseObject;
import com.example.shopappbackend.responses.material.MaterialResponse;
import com.example.shopappbackend.services.material.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<ResponseObject> createMaterial(
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
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Input is invalid !")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .data(materialResponse)
                            .build()
            );
        }
        Material material = materialService.createMaterial(materialDTO);
        materialResponse.setMaterial(material);
        materialResponse.setMessage("Create material is successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Create material is successfully !")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .data(materialResponse)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> updateMaterial(
            @PathVariable Long id,
            @RequestBody MaterialDTO materialDTO
    ) throws DataNotFoundException {

        materialService.updateMaterial(id, materialDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully updated material !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> deleteMaterial(
            @PathVariable Long id
    ) throws DataNotFoundException {

        materialService.deleteMaterialById(id);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully deleted material !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getMaterialById(
            @PathVariable("id") Long materialId
    ) throws DataNotFoundException {

        Material existingMaterial = materialService.getMaterialById(materialId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully get material by id !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(existingMaterial)
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllMaterials() {

        List<Material> categories = materialService.getMaterials();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully get materials !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(categories)
                        .build()
        );
    }
    
}
