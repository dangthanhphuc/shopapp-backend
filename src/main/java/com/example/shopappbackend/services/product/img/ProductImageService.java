package com.example.shopappbackend.services.product.img;

import com.example.shopappbackend.dtos.ProductDTO;
import com.example.shopappbackend.entities.Product;
import com.example.shopappbackend.entities.ProductImage;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.ProductImageRepository;
import com.example.shopappbackend.services.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductImageService implements IProductImageService{
    private final ProductImageRepository productImageRepository;
    @Override
    @Transactional
    public ProductImage deleteProductImage(Long id) throws DataNotFoundException {
        ProductImage existingProductImage = getProductById(id);
        existingProductImage.setDeleted(true);
        productImageRepository.save(existingProductImage);
        return existingProductImage;
    }

    private ProductImage getProductById(Long id) throws DataNotFoundException {
        return productImageRepository.findById(id)
                .filter(productImage -> !productImage.isDeleted())
                .orElseThrow(
                        () -> new DataNotFoundException("Product image not found by id: " + id)
                );
    }
}
