package com.example.shopappbackend.controllers;

import com.example.shopappbackend.services.product.IProductService;
import com.example.shopappbackend.services.product.img.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/product_images")
@RestController
public class ProductImageController {
    private final IProductImageService productImageService;

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProductImage(@PathVariable Long id){
        try{
            productImageService.deleteProductImage(id);
            return ResponseEntity.ok("Product image deleted is successfully!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
