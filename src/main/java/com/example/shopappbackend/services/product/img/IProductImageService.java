package com.example.shopappbackend.services.product.img;


import com.example.shopappbackend.dtos.ProductDTO;
import com.example.shopappbackend.dtos.ProductImageDTO;
import com.example.shopappbackend.entities.ProductImage;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductImageService {
    ProductImage deleteProductImage(Long id) throws DataNotFoundException;
}
